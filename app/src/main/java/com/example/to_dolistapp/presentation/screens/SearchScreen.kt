package com.example.to_dolistapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    // Collect the flow of search results
    val searchResults by searchViewModel
        .searchTasks(searchQuery)
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Tasks, projects, and more") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recently Visited (Static Example)
        Text(
            text = "Recently visited",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        // This row just mimics “Upcoming” and “Today” from your screenshot
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Example “Upcoming”
            IconWithLabel(
                icon = Icons.Outlined.Update,
                label = "Upcoming"
            )
            // Example “Today”
            IconWithLabel(
                icon = Icons.Outlined.EventNote,
                label = "Today"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Results
        if (searchQuery.isNotEmpty()) {
            Text(
                text = "Search Results",
                style = MaterialTheme.typography.titleMedium
            )
        }

        LazyColumn {
            items(searchResults) { task ->
                TaskSearchItem(task = task)
            }
        }
    }
}

// A simple composable for the “Recently visited” row items
@Composable
fun IconWithLabel(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}

// A simple row showing each matching task
@Composable
fun TaskSearchItem(task: Task) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = task.title, style = MaterialTheme.typography.titleSmall)
        if (!task.description.isNullOrEmpty()) {
            Text(text = task.description.orEmpty(), style = MaterialTheme.typography.bodyMedium)
        }
        Divider()
    }
}
