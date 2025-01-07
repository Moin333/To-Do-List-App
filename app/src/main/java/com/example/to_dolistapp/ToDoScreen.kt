package com.example.to_dolistapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_dolistapp.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(viewModel: ToDoViewModel = viewModel()) {
    var taskText by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = taskText,
            onValueChange = { taskText = it },
            label = { Text(text = "Enter Task", style = Typography.bodyMedium) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addTask(taskText)
                taskText = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add Task", style = Typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(viewModel.todoList) { task ->
                TaskItem(task, viewModel::removeTask)
            }
        }
    }
}

@Composable
fun TaskItem(task: String, onDelete: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(task, style = Typography.bodyLarge)
        IconButton(onClick = { onDelete(task) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
        }
    }
}
