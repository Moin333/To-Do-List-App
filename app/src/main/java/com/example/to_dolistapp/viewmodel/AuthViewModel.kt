package com.example.to_dolistapp.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AuthState(
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
)

@Suppress("DEPRECATION")
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _authState.update { AuthState(isLoggedIn = auth.currentUser != null) }
        }
    }

    /**
     * Login with email and password
     */
    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.update { AuthState(isLoggedIn = true) }
                } else {
                    _authState.update { AuthState(isLoggedIn = false, errorMessage = task.exception?.message) }
                }
            }
    }

    /**
     * Register a new user with email and password
     */
    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.update { AuthState(isLoggedIn = true) }
                } else {
                    _authState.update { AuthState(isLoggedIn = false, errorMessage = task.exception?.message) }
                }
            }
    }

    /**
     * Logout the user
     */
    fun logout() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
        _authState.update { AuthState(isLoggedIn = false) }
        println("Logout completed. Auth state updated: isLoggedIn = false")
    }

    /**
     * Check if user is already logged in
     */
    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    /**
     * Get the Google Sign-In intent
     */
    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    /**
     * Handle the result of Google Sign-In
     */
    fun handleGoogleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)

            account?.let {
                val credential = GoogleAuthProvider.getCredential(it.idToken, null)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            _authState.update { AuthState(isLoggedIn = true) }
                        } else {
                            _authState.update {
                                AuthState(
                                    isLoggedIn = false,
                                    errorMessage = authTask.exception?.message ?: "Authentication failed"
                                )
                            }
                        }
                    }
            } ?: run {
                _authState.update { AuthState(isLoggedIn = false, errorMessage = "Google Sign-In failed") }
            }
        } catch (e: ApiException) {
            _authState.update {
                AuthState(isLoggedIn = false, errorMessage = "Google Sign-In failed: ${e.localizedMessage}")
            }
        } catch (e: Exception) {
            _authState.update {
                AuthState(isLoggedIn = false, errorMessage = e.message)
            }
        }
    }
}
