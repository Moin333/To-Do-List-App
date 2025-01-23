package com.example.to_dolistapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_dolistapp.presentation.screens.AddTaskScreen
import com.example.to_dolistapp.presentation.screens.AuthScreen
import com.example.to_dolistapp.presentation.screens.BrowseScreen
import com.example.to_dolistapp.presentation.screens.LoginScreen
import com.example.to_dolistapp.presentation.screens.SearchScreen
import com.example.to_dolistapp.presentation.screens.SignupScreen
import com.example.to_dolistapp.presentation.screens.ToDoScreen
import com.example.to_dolistapp.presentation.screens.UpcomingScreen
import com.example.to_dolistapp.presentation.widgets.BottomNavigationBar
import com.example.to_dolistapp.presentation.widgets.CustomTopAppBar
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
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route
                        if (currentRoute in listOf("todo", "upcoming", "search", "browse")) {
                            CustomTopAppBar(currentRoute = currentRoute ?: "") { action ->
                                // Handle menu clicks here if needed
                            }
                        }
                    },
                    floatingActionButton = {
                        val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route
                        if (currentRoute in listOf("todo", "upcoming", "search", "browse")) {
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate("add_task")
                                },
                                content = {
                                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        }
                    },
                    bottomBar = {
                        val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route
                        if (currentRoute in listOf("todo", "upcoming", "search", "browse")) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        authViewModel = authViewModel,
                        navController = navController,
                        innerPadding = innerPadding
                    ) { intent ->
                        googleSignInLauncher.launch(intent)
                    }
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
fun AppNavigation(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues,
    startActivityForResult: (Intent) -> Unit
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            if (navController.currentDestination?.route != "todo") {
                navController.navigate("todo") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        } else {
            if (navController.currentDestination?.route !in listOf("auth", "login", "signup")) {
                navController.navigate("auth") {
                    popUpTo("todo") { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "auth",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("auth") {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel,
                startActivityForResult = startActivityForResult
            )
        }
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("signup") {
            SignupScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("todo") {
            ToDoScreen(navController = navController)
        }
        composable("upcoming") {
            UpcomingScreen(navController = navController)
        }
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable("browse") {
            BrowseScreen(navController = navController)
        }
        composable("add_task") {
            AddTaskScreen(navController = navController)
        }
    }
}

