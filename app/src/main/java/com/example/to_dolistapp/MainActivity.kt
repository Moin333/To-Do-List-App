package com.example.to_dolistapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_dolistapp.presentation.screen.AuthScreen
import com.example.to_dolistapp.presentation.screen.ToDoScreen
import com.example.to_dolistapp.ui.theme.ToDoListAppTheme
import com.example.to_dolistapp.viewmodel.AuthViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInLauncher: androidx.activity.result.ActivityResultLauncher<Intent>
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize CallbackManager for Facebook
        callbackManager = CallbackManager.Factory.create()

        // Initialize Google Sign-In Launcher
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            authViewModel.handleGoogleSignInResult(data)
        }

        setContent {
            ToDoListAppTheme {
                AppNavigation(authViewModel) { intent ->
                    googleSignInLauncher.launch(intent)
                }
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

@Composable
fun AppNavigation(authViewModel: AuthViewModel, startActivityForResult: (Intent) -> Unit) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Reactively observe authentication state using LaunchedEffect
    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            if (navController.currentDestination?.route != "todo") {
                navController.navigate("todo") {
                    popUpTo("login") { inclusive = true }
                }
            }
        } else {
            if (navController.currentDestination?.route != "login") {
                navController.navigate("login") {
                    popUpTo("todo") { inclusive = true }
                }
            }
        }
    }

    NavHost(navController, startDestination = "login") {
        composable("login") {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel,
                startActivityForResult = startActivityForResult
            )
        }
        composable("todo") { ToDoScreen(navController) }
    }
}
