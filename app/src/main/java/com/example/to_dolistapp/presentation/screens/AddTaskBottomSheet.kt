package com.example.to_dolistapp.presentation.screens

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTaskBottomSheet(
    onDismiss: () -> Unit,
    navController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        // No extra insets.
        contentWindowInsets = { androidx.compose.foundation.layout.WindowInsets(0) },
        dragHandle = {
            // A simple drag handle can be placed here.
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                // Customize the drag handle as needed.
            ) { }
        },
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .imeNestedScroll()
            .navigationBarsPadding(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        tonalElevation = 2.dp,
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
    ) {
        // Display the actual add task content.
        AddTaskContent(
            onDismiss = onDismiss,
            navController = navController
        )
    }
}
