package com.example.to_dolistapp.presentation.widgets

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli()
            )
            
            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    onDateSelected(
                        LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                    )
                }
            }
            
            DatePicker(
                state = datePickerState,
                modifier = modifier
            )
        }
    }
}

@Composable
fun DatePickerButton(
    selectedDate: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    dateFormatter: DateTimeFormatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy") }
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(selectedDate.format(dateFormatter))
    }
}