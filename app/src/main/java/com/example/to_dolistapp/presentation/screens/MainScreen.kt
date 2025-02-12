package com.example.to_dolistapp.presentation.screens

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.to_dolistapp.presentation.navigation.AppNavigation
import com.example.to_dolistapp.presentation.widgets.BottomNavigationBar
import com.example.to_dolistapp.presentation.widgets.CustomTopAppBar
import com.example.to_dolistapp.viewmodel.AuthViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    googleSignInLauncher: (Intent) -> Unit
) {
    // State controlling the visibility of the overlay bottom sheet.
    var showAddTaskSheet by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    // Observe the current navigation back stack entry.
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        // Top app bar appears on main routes.
        topBar = {
            if (currentRoute in listOf("todo", "upcoming", "search", "browse", "settings")) {
                CustomTopAppBar(
                    currentRoute = currentRoute ?: "",
                    onMenuClick = { action ->
                        when (action) {
                            "settings" -> navController.navigate("settings")
                            "back" -> navController.popBackStack()
                        }
                    },
                    onBackPress = { navController.popBackStack() }
                )
            }
        },
        // FAB is shown on main routes.
        floatingActionButton = {
            if (currentRoute in listOf("todo", "upcoming", "search", "browse")) {
                FloatingActionButton(
                    onClick = { showAddTaskSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        },
        // Bottom navigation bar appears on main routes.
        bottomBar = {
            if (currentRoute in listOf("todo", "upcoming", "search", "browse")) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues: PaddingValues ->
        // Main navigation graph.
        AppNavigation(
            authViewModel = authViewModel,
            navController = navController,
            innerPadding = paddingValues,
            startActivityForResult = googleSignInLauncher
        )

        // Overlay the Add Task bottom sheet on top of current content.
        if (showAddTaskSheet) {
            AddTaskBottomSheet(
                onDismiss = { showAddTaskSheet = false },
                navController = navController
            )
        }
    }
}
