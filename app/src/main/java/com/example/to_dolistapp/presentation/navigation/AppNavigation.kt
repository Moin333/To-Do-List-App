package com.example.to_dolistapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.to_dolistapp.presentation.screens.ActivityLogScreen
import com.example.to_dolistapp.presentation.screens.AddProjects
import com.example.to_dolistapp.presentation.screens.AuthScreen
import com.example.to_dolistapp.presentation.screens.BrowseScreen
import com.example.to_dolistapp.presentation.screens.BrowseTemplatesScreen
import com.example.to_dolistapp.presentation.screens.CompletedScreen
import com.example.to_dolistapp.presentation.screens.FiltersAndLabelsScreen
import com.example.to_dolistapp.presentation.screens.InboxScreen
import com.example.to_dolistapp.presentation.screens.LoginScreen
import com.example.to_dolistapp.presentation.screens.ManageProjectsScreen
import com.example.to_dolistapp.presentation.screens.SearchScreen
import com.example.to_dolistapp.presentation.screens.SettingsScreen
import com.example.to_dolistapp.presentation.screens.SignupScreen
import com.example.to_dolistapp.presentation.screens.ToDoScreen
import com.example.to_dolistapp.presentation.screens.UpcomingScreen
import com.example.to_dolistapp.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues,
    startActivityForResult: (android.content.Intent) -> Unit
) {
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
        composable("settings") {
            SettingsScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("inbox") {
            InboxScreen(navController = navController)
        }
        composable("filters_labels") {
            FiltersAndLabelsScreen(navController = navController)
        }
        composable("completed") {
            CompletedScreen(navController = navController)
        }
        composable("add_projects") {
            AddProjects(navController = navController)
        }
        composable("manage_projects") {
            ManageProjectsScreen(navController = navController)
        }
        composable("browse_templates") {
            BrowseTemplatesScreen(navController = navController)
        }
        composable("activity_log") {
            ActivityLogScreen(navController = navController)
        }
    }
}
