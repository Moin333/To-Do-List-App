package com.example.to_dolistapp.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.material3.ListItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UpcomingScreen(
    navController: NavController,
) {
    // Keep "today" stable, so it doesn't change on recompose:
    val today by remember { mutableStateOf(LocalDate.now()) }
    var selectedDate by remember { mutableStateOf(today) }
    var isExpanded by remember { mutableStateOf(false) }

    // Pre-calculate date formatter
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM dd • EEEE") }
    // Static list of short weekday labels:
    val weekDays = listOf("M", "T", "W", "T", "F", "S", "S")

    // Build the header text (e.g. "February 2025")
    val headerText = remember(selectedDate) {
        val monthName = selectedDate.month
            .getDisplayName(TextStyle.FULL, Locale.getDefault())
            .replaceFirstChar { it.uppercase() }
        "$monthName ${selectedDate.year}"
    }

    // Add drag state
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val dragThreshold = 50f // Threshold to trigger expand/collapse

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        // Calendar Header with adjusted padding
        CalendarHeader(
            headerText = headerText,
            isExpanded = isExpanded,
            onExpandClick = { isExpanded = !isExpanded },
            modifier = Modifier.padding(horizontal = 20.dp) // Increased to match app bar padding
        )

        // Week Days Header
        WeekDaysHeader(
            weekDays = weekDays,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 4.dp)
        )

        // Calendar Grid with Integrated Notch
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column {
                // Calendar Content
                AnimatedContent(
                    targetState = isExpanded,
                    transitionSpec = {
                        (fadeIn() + slideInVertically()) with (fadeOut() + slideOutVertically())
                    },
                    label = "CalendarExpandTransition",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                ) { expanded ->
                    if (expanded) {
                        MonthCalendarGrid(
                            selectedDate = selectedDate,
                            today = today,
                            onDateSelected = { selectedDate = it }
                        )
                    } else {
                        WeekCalendarGrid(
                            selectedDate = selectedDate,
                            today = today,
                            onDateSelected = { selectedDate = it }
                        )
                    }
                }

                // Integrated Calendar Notch
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    if (abs(dragOffset) > dragThreshold) {
                                        isExpanded = dragOffset > 0
                                        dragOffset = 0f
                                    }
                                },
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffset += dragAmount
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        // Notch Handle
                        Surface(
                            modifier = Modifier
                                .width(32.dp)
                                .height(4.dp)
                                .alpha(if (isExpanded) 0.8f else 0.4f),
                            shape = RoundedCornerShape(2.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ) {}

                        Spacer(modifier = Modifier.height(8.dp))

                        // Divider
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(0.12f),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Tasks List
        TasksList(
            selectedDate = selectedDate,
            dateFormatter = dateFormatter,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
private fun CalendarHeader(
    headerText: String,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onExpandClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (isExpanded) 
                    MaterialTheme.colorScheme.primary 
                else MaterialTheme.colorScheme.onBackground,
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .height(36.dp)
                .offset(x = (-16).dp), // Negative offset to align with app bar text
            contentPadding = PaddingValues(horizontal = 12.dp),
            interactionSource = remember { MutableInteractionSource() }
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
                Icon(
                    imageVector = if (isExpanded) 
                        Icons.Default.ArrowDropUp 
                    else Icons.Default.ArrowDropDown,
                    contentDescription = if (isExpanded) 
                        "Collapse Calendar" 
                    else "Expand Calendar",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * A row of short weekday labels: M, T, W, T, F, S, S.
 */
@Composable
private fun WeekDaysHeader(weekDays: List<String>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDays.forEach { day ->
            Text(
                text = day,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(36.dp)
            )
        }
    }
}

/**
 * Shows tasks or events for the currently selected date.
 */
@Composable
private fun TasksList(
    selectedDate: LocalDate,
    dateFormatter: DateTimeFormatter,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            ListItem(
                headlineContent = {
                    Text(
                        text = selectedDate.format(dateFormatter),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Example: Show a "Holiday" item if it's Feb 23
        if (selectedDate.dayOfMonth == 23 && selectedDate.monthValue == 2) {
            item {
                HolidayItem()
            }
        }

        // Add more items if needed...
    }
}

/**
 * A single "holiday" or "event" item.
 * Example placeholder for special days.
 */
@Composable
private fun HolidayItem() {
    ListItem(
        headlineContent = {
            Text(
                "Maharishi Dayanand Saraswati Jayanti",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Spacer(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    )
}

/**
 * Week view: Show 7 days (Mon to Sun) based on the selected date's week.
 */
@Composable
private fun WeekCalendarGrid(
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // Compute the Monday of the selected date's week
    val firstDayOfWeek = remember(selectedDate) {
        selectedDate.with(DayOfWeek.MONDAY)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (dayOffset in 0..6) {
            val currentDate = firstDayOfWeek.plusDays(dayOffset.toLong())
            DayCell(
                date = currentDate,
                selectedDate = selectedDate,
                today = today,
                onDateSelected = onDateSelected
            )
        }
    }
}

/**
 * Month view: 6 rows × 7 columns.
 * The "grid" starts from the Monday of the 1st row that includes day 1 of the month.
 */
@Composable
private fun MonthCalendarGrid(
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // Start from the 1st day of the selected month
    val firstDayOfMonth = remember(selectedDate) {
        selectedDate.withDayOfMonth(1)
    }
    // Shift back to Monday
    val shiftBack = firstDayOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value
    val firstDayOfGrid = remember(firstDayOfMonth) {
        firstDayOfMonth.minusDays(if (shiftBack < 0) shiftBack + 7L else shiftBack.toLong())
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(6) { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(7) { day ->
                    val currentDate = firstDayOfGrid.plusDays((week * 7 + day).toLong())
                    DayCell(
                        date = currentDate,
                        selectedDate = selectedDate,
                        today = today,
                        onDateSelected = onDateSelected,
                        isMonthView = true
                    )
                }
            }
        }
    }
}

/**
 * Single day cell used by both the Week and Month views.
 *
 * If [isMonthView] is true, we also dim days not in the selected month.
 */
@Composable
private fun DayCell(
    date: LocalDate,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    isMonthView: Boolean = false
) {
    val isSelected = date == selectedDate
    val isToday = date == today
    val isPastDate = date.isBefore(today)
    val isCurrentMonth = date.month == selectedDate.month

    var isTapped by remember { mutableStateOf(false) }

    // Ripple effect animation
    val rippleSize by animateDpAsState(
        targetValue = if (isTapped) 44.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "Ripple Animation"
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                isTapped = true
                onDateSelected(date)
            },
        contentAlignment = Alignment.Center
    ) {
        // Ripple Effect
        if (rippleSize > 0.dp) {
            Box(
                modifier = Modifier
                    .size(rippleSize)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape)
            )
        }

        when {
            isSelected -> {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            isToday -> {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Transparent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                val textColor = when {
                    isMonthView && !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    isPastDate -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    else -> MaterialTheme.colorScheme.onSurface
                }
                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 16.sp,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Reset ripple animation after it's done
        LaunchedEffect(isTapped) {
            if (isTapped) {
                delay(300)
                isTapped = false
            }
        }
    }
}
