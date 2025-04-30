package com.example.to_dolistapp.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(currentRoute: String, onMenuClick: (String) -> Unit, onBackPress: (() -> Unit)? = null) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = when (currentRoute) {
                    "todo" -> "Today"
                    "upcoming" -> "Upcoming"
                    "search" -> "Search"
                    "browse" -> "xyz"
                    "settings" -> "Settings"
                    else -> ""
                }
            )
        },
        navigationIcon = {
            if (currentRoute == "settings" && onBackPress != null) {
                IconButton(onClick = onBackPress) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            when (currentRoute) {
                "todo", "upcoming" -> {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        offset = DpOffset(x = 0.dp, y = (-48).dp), // Adjusted to start from the top right corner
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = MenuDefaults.ShadowElevation,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            )
                            .width(200.dp)
                    ) {
                        DropdownMenuItem(
                            onClick = { onMenuClick("view"); showMenu = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.ViewAgenda,
                                    contentDescription = "View"
                                )
                            },
                            text = { Text("View") }
                        )
                        DropdownMenuItem(
                            onClick = { onMenuClick("select_tasks"); showMenu = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.AutoAwesomeMotion,
                                    contentDescription = "Select Tasks"
                                )
                            },
                            text = { Text("Select tasks") }
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            thickness = 0.8.dp
                        )
                        DropdownMenuItem(
                            onClick = { onMenuClick("activity_log"); showMenu = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.History,
                                    contentDescription = "Activity Log"
                                )
                            },
                            text = { Text("Activity log") }
                        )
                    }
                }
                "search" -> {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        offset = DpOffset(x = 0.dp, y = (-48).dp), // Adjusted to start from the top right corner
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = MenuDefaults.ShadowElevation,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            )
                            .width(200.dp)
                    ) {
                        DropdownMenuItem(
                            onClick = { onMenuClick("activity_log"); showMenu = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.History,
                                    contentDescription = "Activity Log"
                                )
                            },
                            text = { Text("Activity log") }
                        )
                    }
                }
                "browse" -> {
                    IconButton(onClick = { onMenuClick("notifications") }) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                    IconButton(onClick = { onMenuClick("settings") }) {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}