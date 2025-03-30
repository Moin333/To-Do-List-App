package com.example.to_dolistapp.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.compose.ui.window.PopupProperties
import com.example.to_dolistapp.model.entities.Task
import com.example.to_dolistapp.presentation.widgets.CalendarView
import com.example.to_dolistapp.presentation.widgets.DatePickerButton
import com.example.to_dolistapp.viewmodel.ProjectViewModel
import com.example.to_dolistapp.viewmodel.ToDoViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskContent(
    onDismiss: () -> Unit,
    navController: NavHostController,
    todoViewModel: ToDoViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    projectViewModel: ProjectViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf("Inbox") }

    // Collect project names from ProjectViewModel.

    val projectNames = listOf("Inbox", "Work", "Personal")

    var showProjectMenu by remember { mutableStateOf(false) }
    var showProjectSuggestions by remember { mutableStateOf(false) }
    var triggerPattern by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    // Compute maximum content height (66% of the screen)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val maxContentHeight = screenHeight * 0.66f

    // Request focus and show keyboard on launch
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // Handle back press to dismiss
    BackHandler {
        scope.launch {
            keyboardController?.hide()
            focusManager.clearFocus()
            onDismiss()
        }
    }

    // We wrap the entire content in a Box so that our dropdown menus
    // can extend freely (especially with clippingDisabled = false).
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxContentHeight)
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title TextField + suggestions
            Box(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = title,
                    onValueChange = { newValue ->
                        title = newValue
                        // Detect a trigger at the end (e.g. "#" or "/")
                        val regex = """([#/])(\w*)$""".toRegex()
                        val matchResult = regex.find(newValue)
                        if (matchResult != null) {
                            showProjectSuggestions = true
                            triggerPattern = matchResult.groupValues[1] // "#" or "/"
                        } else {
                            showProjectSuggestions = false
                        }
                    },
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

                // Dropdown for inline project suggestions
                if (showProjectSuggestions) {
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = { showProjectSuggestions = false },
                        // Removed large negative offset; you can tweak if needed:
                        offset = DpOffset(0.dp, 0.dp),
                        modifier = Modifier.zIndex(10f),
                        properties = PopupProperties(
                            clippingEnabled = false,
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        projectNames.forEach { name ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    // Replace the trigger pattern (e.g. "#" or "/")
                                    // and trailing characters with the project name.
                                    val regex = """([#/])\w*$""".toRegex()
                                    title = regex.replace(title) { name }
                                    selectedProject = name
                                    showProjectSuggestions = false
                                }
                            )
                        }
                    }
                }
            }

            // Description
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

            // Row with date picker, priority, reminders
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

            // Calendar dialog
            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                showDialog = showDatePicker,
                onDismiss = { showDatePicker = false }
            )

            // Bottom row: Project selection + Add button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Project button and dropdown
                // Project button and dropdown
                Box {
                    OutlinedButton(
                        onClick = { showProjectMenu = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    ) {
                        Text(selectedProject)
                    }

                    DropdownMenu(
                        expanded = showProjectMenu,
                        onDismissRequest = { showProjectMenu = false },
                        // Adjusting offset to try and bring the dropdown into view.
                        offset = DpOffset(0.dp, (-40).dp),
                        modifier = Modifier.zIndex(20f),
                        properties = PopupProperties(
                            clippingEnabled = false,
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        projectNames.forEach { name ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    selectedProject = name
                                    showProjectMenu = false
                                }
                            )
                        }
                    }
                }


                // Add Task button
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            scope.launch {
                                todoViewModel.addTask(
                                    Task().apply {
                                        this.title = title
                                        this.description = description
                                        this.date = selectedDate.toRealmInstant()
                                        this.projectName = selectedProject
                                    }
                                )
                                onDismiss()
                            }
                        }
                    },
                    contentPadding = PaddingValues(
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
    return RealmInstant.from(
        this.atStartOfDay(ZoneOffset.UTC).toEpochSecond(),
        0
    )
}
