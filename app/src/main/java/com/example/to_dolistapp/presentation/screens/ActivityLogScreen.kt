package com.example.to_dolistapp.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.to_dolistapp.viewmodel.ToDoViewModel
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ActivityLogScreen(
    navController: NavController,
    toDoViewModel: ToDoViewModel = hiltViewModel()
) {
    // Collect ALL tasks from DB or create a separate flow for isCompleted tasks
    val completedTasks by toDoViewModel.completedTasks.collectAsState()

    Text(text = "Activity Log (Placeholder)")

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(completedTasks) { task ->
            Text(text = "Completed: ${task.title}")
        }
    }
}
