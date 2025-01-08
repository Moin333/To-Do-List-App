package com.example.to_dolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_dolistapp.presentation.screen.AuthScreen
import com.example.to_dolistapp.presentation.screen.ToDoScreen
import com.example.to_dolistapp.ui.theme.ToDoListAppTheme
import com.example.to_dolistapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val startDestination = if (authViewModel.isUserLoggedIn()) "todo" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { AuthScreen(navController) }
        composable("todo") { ToDoScreen(navController) }
    }
}
