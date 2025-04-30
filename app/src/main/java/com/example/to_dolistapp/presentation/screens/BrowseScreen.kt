package com.example.to_dolistapp.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BrowseScreen(
    navController: NavController
) {
    // Toggles the expanded/collapsed state of the My Projects section
    var myProjectsExpanded by remember { mutableStateOf(true) }

    // Example data: you might fetch from Realm or a ViewModel
    val myProjects = listOf(
        ProjectUIModel("My work", 4),
        ProjectUIModel("Education", 4)
    )

    // We’ll show “Inbox,” “Filters & Labels,” “Completed,” “My Projects” (expanded/collapsed),
    // and finally a separate “Browse templates” row at the bottom.
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1) Inbox
        item {
            BrowseRowItem(
                label = "Inbox",
                icon = Icons.Outlined.Inbox,
                onClick = { navController.navigate("inbox") }
            )
        }

        // 2) Filters & Labels
        item {
            BrowseRowItem(
                label = "Filters & Labels",
                icon = Icons.Outlined.Label,
                onClick = { navController.navigate("filters_labels") }
            )
        }

        // 3) Completed
        item {
            BrowseRowItem(
                label = "Completed",
                icon = Icons.Outlined.CheckCircle,
                onClick = { navController.navigate("completed") }
            )
        }

        // 4) My Projects row (clickable to toggle)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable { myProjectsExpanded = !myProjectsExpanded }
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Ensures even spacing
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "My Projects" text (also toggles on click)
                Text(
                    text = "My Projects",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                )
                // Plus icon on the right side
                IconButton(onClick = { navController.navigate("add_projects") }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Project",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                // Arrow icon (Up if expanded, Down if collapsed)
                Icon(
                    imageVector = if (myProjectsExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Toggle My Projects",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { myProjectsExpanded = !myProjectsExpanded }
                )
            }

            // If expanded, show the user’s projects plus "Manage projects"
            if (myProjectsExpanded) {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // List of projects
                    myProjects.forEach { project ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // e.g., navController.navigate("project_detail/${project.name}")
                                }
                                .padding(horizontal = 16.dp)
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Example: "# My work" + count
                            Text(
                                text = "#  ${project.name}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            // Show the number of tasks
                            Text(
                                text = project.taskCount.toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                // Manage projects row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("manage_projects") }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = "Manage projects",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // 5) Browse templates (now separate from My Projects)
        item {
            BrowseRowItem(
                label = "Browse templates",
                icon = Icons.Default.Add, // or any other icon you prefer
                onClick = { navController.navigate("browse_templates") }
            )
        }
    }
}

/**
 * A single row item for top-level items (Inbox, Filters & Labels, Completed, etc.).
 * Icons are tinted with the app's primary color.
 */
@Composable
fun BrowseRowItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * Represents a user's project with a name and task count.
 */
data class ProjectUIModel(val name: String, val taskCount: Int)
