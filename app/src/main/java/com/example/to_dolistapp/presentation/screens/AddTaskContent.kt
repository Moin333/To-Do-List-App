package com.example.to_dolistapp.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.presentation.widgets.CalendarView
import com.example.to_dolistapp.presentation.widgets.DatePickerButton
import com.example.to_dolistapp.viewmodel.ToDoViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTaskContent(
    onDismiss: () -> Unit,
    navController: NavHostController,
    todoViewModel: ToDoViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    // Compute maximum content height (66% of the screen)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val maxContentHeight = screenHeight * 0.66f

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // Handle back press to dismiss the bottom sheet.
    BackHandler {
        scope.launch {
            keyboardController?.hide()
            focusManager.clearFocus()
            onDismiss()
        }
    }

    // Constrain the content height using Box with heightIn.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxContentHeight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = {
                    Text(
                        "e.g. Take kids to the park after work tom",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        "Description",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )

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
                    OutlinedButton(
                        onClick = { /* Priority logic */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    ) {
                        Text("Priority")
                    }
                }
                item {
                    OutlinedButton(
                        onClick = { /* Reminder logic */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    ) {
                        Text("Reminders")
                    }
                }
            }

            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                showDialog = showDatePicker,
                onDismiss = { showDatePicker = false }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { /* Inbox selection */ },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                ) {
                    Text("Inbox")
                }

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            scope.launch {
                                todoViewModel.addTask(
                                    Task().apply {
                                        this.title = title
                                        this.description = description
                                        this.date = selectedDate.toRealmInstant()
                                    }
                                )
                                onDismiss()
                            }
                        }
                    },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Add Task",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

// Extension function to convert LocalDate to RealmInstant.
fun LocalDate.toRealmInstant(): RealmInstant {
    return RealmInstant.from(this.atStartOfDay(ZoneOffset.UTC).toEpochSecond(), 0)
}
