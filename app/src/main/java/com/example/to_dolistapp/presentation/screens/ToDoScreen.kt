package com.example.to_dolistapp.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.model.entities.toLocalDate
import com.example.to_dolistapp.presentation.widgets.EditTaskBottomSheet
import com.example.to_dolistapp.viewmodel.ToDoViewModel
import java.time.LocalDate
import java.util.Locale

@Composable
fun ToDoScreen(
    navController: NavController,
    todoViewModel: ToDoViewModel = hiltViewModel()
) {
    // Track which task is selected for editing
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    // Show the edit bottom sheet when a task is selected
    selectedTask?.let { task ->
        EditTaskBottomSheet(
            taskToEdit = task,
            onDismiss = { selectedTask = null }
        )
    }

    // Display active tasks only
    ToDoContent(
        todoViewModel = todoViewModel,
        onTaskClicked = { task -> selectedTask = task },
        navController = navController
    )
}

@Composable
fun ToDoContent(
    todoViewModel: ToDoViewModel,
    onTaskClicked: (Task) -> Unit,
    navController: NavController
) {
    // Collect only active (not completed) tasks
    val activeTasks by todoViewModel.activeTasks.collectAsState()

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(activeTasks) { task ->
            TaskItem(
                task = task,
                onComplete = { todoViewModel.completeTask(task.id) },
                onDelete = { todoViewModel.removeTask(task.id) },
                onClick = { onTaskClicked(task) }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    // Determine the date label (e.g., "Overdue", "Today", or formatted date)
    val dateLabel = formatDueDate(task.date)

    Column(modifier = Modifier.fillMaxWidth()) {
        // Show date label if not empty
        if (dateLabel.isNotEmpty()) {
            Text(
                text = dateLabel,
                style = MaterialTheme.typography.labelMedium,
                color = when (dateLabel) {
                    "Overdue" -> MaterialTheme.colorScheme.error
                    "Today" -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Complete Task Button (hollow circular icon)
            IconButton(
                onClick = onComplete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircleOutline,
                    contentDescription = "Complete Task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Task Title & Description (clickable for editing)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick() }
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (!task.description.isNullOrEmpty()) {
                    Text(
                        text = task.description ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Delete Task Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )
    }
}

fun formatDueDate(date: io.realm.kotlin.types.RealmInstant?): String {
    if (date == null) return ""

    val localDate = date.toLocalDate()
    val today = LocalDate.now()

    return when {
        localDate.isBefore(today) -> "Overdue"
        localDate.isEqual(today) -> "Today"
        else -> {
            val dayOfMonth = localDate.dayOfMonth
            val monthAbbrev = localDate.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())
            val dayOfWeek = localDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
            "$monthAbbrev $dayOfMonth • $dayOfWeek"
        }
    }
}
