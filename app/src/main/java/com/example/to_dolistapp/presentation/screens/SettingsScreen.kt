package com.example.to_dolistapp.presentation.screens

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.to_dolistapp.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // Retrieve the user’s email from AuthViewModel
    val userEmail = authViewModel.getCurrentUserEmail() ?: "No Email"

    // We'll create data for each settings item in a structured way
    val topItems = listOf(
        SettingItemData("Upgrade to Pro", Icons.Outlined.Star),
        SettingItemData("Account", Icons.Outlined.Person),
        SettingItemData("General", Icons.Outlined.Settings),
        SettingItemData("Calendar", Icons.Outlined.CalendarMonth)
    )

    val personalizationItems = listOf(
        SettingItemData("Theme", Icons.Outlined.ColorLens, subLabel = "Dark"),
        SettingItemData("App icon", Icons.Outlined.Android, subLabel = "Todolist"),
        SettingItemData("Navigation bar", Icons.Outlined.ViewStream),
        SettingItemData("Navigation menu", Icons.Outlined.Menu),
        SettingItemData("Quick Add", Icons.Outlined.AddCircleOutline)
    )

    val productivityItems = listOf(
        SettingItemData("Productivity", Icons.Outlined.Timeline),
        SettingItemData("Reminders", Icons.Outlined.NotificationsNone),
        SettingItemData("Notifications", Icons.Outlined.Notifications)
    )

    val moreItems = listOf(
        SettingItemData("Support", Icons.Outlined.Help),
        SettingItemData("About", Icons.Outlined.Info),
        SettingItemData("What’s new", Icons.Outlined.NewReleases)
        // We'll handle "Logout" separately to show the email
    )

    // LazyColumn to hold all sections
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Top items
        items(topItems) { itemData ->
            SettingRowItem(
                itemData = itemData,
                onClick = {
                    // Handle top items click
                    // e.g., navController.navigate("account") if itemData.title == "Account"
                }
            )
        }

        item {
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        }

        // Personalization section
        item {
            SectionHeader("Personalization")
        }
        items(personalizationItems) { itemData ->
            SettingRowItem(
                itemData = itemData,
                onClick = {
                    // e.g., navController.navigate("theme") if itemData.title == "Theme"
                }
            )
        }

        item {
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        }

        // Productivity section
        item {
            SectionHeader("Productivity")
        }
        items(productivityItems) { itemData ->
            SettingRowItem(
                itemData = itemData,
                onClick = {
                    // e.g., navController.navigate("theme") if itemData.title == "Theme"
                }
            )
        }

        item {
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        }

        // More section
        item {
            SectionHeader("More")
        }
        items(moreItems) { itemData ->
            SettingRowItem(
                itemData = itemData,
                onClick = {
                    // e.g., navController.navigate("about") if itemData.title == "About"
                }
            )
        }

        // Finally, the Logout row
        item {
            LogoutRow(
                userEmail = userEmail,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("auth") {
                        popUpTo("settings") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SettingRowItem(
    itemData: SettingItemData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }    // The entire row is clickable
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = itemData.icon,
            contentDescription = itemData.title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = itemData.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (!itemData.subLabel.isNullOrEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = itemData.subLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun LogoutRow(
    userEmail: String,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLogout() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.ExitToApp,
            contentDescription = "Logout",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Log out",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = userEmail,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


// Data class for each row
data class SettingItemData(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val subLabel: String? = null
)
