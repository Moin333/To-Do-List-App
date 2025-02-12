package com.example.to_dolistapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.to_dolistapp.presentation.screens.MainScreen
import com.example.to_dolistapp.ui.theme.ToDoListAppTheme
import com.example.to_dolistapp.viewmodel.AuthViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInLauncher: androidx.activity.result.ActivityResultLauncher<Intent>
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Facebook CallbackManager
        callbackManager = CallbackManager.Factory.create()

        // Initialize Google Sign-In Launcher
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            authViewModel.handleGoogleSignInResult(result.data)
        }

        setContent {
            ToDoListAppTheme {
                // Pass in the authViewModel and a lambda to launch Google sign-in.
                MainScreen(
                    authViewModel = authViewModel,
                    googleSignInLauncher = { intent -> googleSignInLauncher.launch(intent) }
                )
            }
        }

        // Register Facebook Login Callback
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    authViewModel.handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    authViewModel.updateErrorMessage("Facebook Login Cancelled")
                }

                override fun onError(error: FacebookException) {
                    authViewModel.updateErrorMessage("Facebook Login Failed: ${error.message}")
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun startFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("email", "public_profile")
        )
    }
}
