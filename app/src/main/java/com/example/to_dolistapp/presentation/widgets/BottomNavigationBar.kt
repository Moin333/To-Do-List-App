package com.example.to_dolistapp.presentation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentDestination by navController.currentBackStackEntryAsState()

    Column {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceTint,
            tonalElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            val items = listOf(
                BottomNavItem("todo", Icons.Filled.Home, Icons.Outlined.Home, "To-Do"),
                BottomNavItem("upcoming", Icons.Filled.Event, Icons.Outlined.Event, "Upcoming"),
                BottomNavItem("search", Icons.Filled.Search, Icons.Outlined.Search, "Search"),
                BottomNavItem("browse", Icons.Filled.Explore, Icons.Outlined.Explore, "Browse")
            )

            items.forEach { item ->
                val isSelected = currentDestination?.destination?.hierarchy?.any {
                    it.route == item.route
                } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                            contentDescription = item.label
                        )
                    },
                    label = { Text(item.label) },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val label: String
)
