package com.example.to_dolistapp.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
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
    private val googleSignInClient: GoogleSignInClient,
    private val loginManager: LoginManager,
    private val callbackManager: CallbackManager
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> get() = _authState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun updateErrorMessage(message: String?) {
        _authState.update { currentState ->
            currentState.copy(errorMessage = message)
        }
    }

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _authState.update { AuthState(isLoggedIn = auth.currentUser != null) }
        }
    }


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

    fun logout() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
        loginManager.logOut()
        _authState.update { AuthState(isLoggedIn = false) }
    }

    fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

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

    fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign-in successful
                    _authState.update {
                        AuthState(
                            isLoggedIn = true,
                            errorMessage = null // Clear any previous error messages
                        )
                    }
                } else {
                    // Sign-in failed
                    _authState.update {
                        AuthState(
                            isLoggedIn = false,
                            errorMessage = task.exception?.message ?: "Unknown error occurred"
                        )
                    }
                }
            }
    }

}
