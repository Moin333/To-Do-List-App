package com.example.to_dolistapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.presentation.widgets.CalendarView
import com.example.to_dolistapp.presentation.widgets.DatePickerButton
import com.example.to_dolistapp.viewmodel.ToDoViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavHostController, todoViewModel: ToDoViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Task Description") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    DatePickerButton(
                        selectedDate = selectedDate,
                        onClick = { showDatePicker = true }
                    )
                }
                
                item {
                    OutlinedButton(onClick = { /* Priority Picker logic */ }) {
                        Text("Priority")
                    }
                }
                
                item {
                    OutlinedButton(onClick = { /* Reminder Picker logic */ }) {
                        Text("Reminders")
                    }
                }
                
                item {
                    OutlinedButton(onClick = { /* Dropdown Menu logic */ }) {
                        Text("More Options")
                    }
                }
            }

            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                showDialog = showDatePicker,
                onDismiss = { showDatePicker = false }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        if (title.isNotBlank()) {
                            todoViewModel.addTask(Task().apply {
                                this.title = title
                                this.description = description
                                this.date = selectedDate.toRealmInstant()
                            })
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Task")
            }
        }
    }
}

// Extension function to convert LocalDate to RealmInstant
fun LocalDate.toRealmInstant(): RealmInstant {
    return RealmInstant.from(this.atStartOfDay(ZoneOffset.UTC).toEpochSecond(), 0)
}