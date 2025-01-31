package com.example.to_dolistapp.presentation.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.to_dolistapp.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    Button(
        onClick = {
            authViewModel.logout()
            navController.navigate("auth") { popUpTo("settings") { inclusive = true } }
        }
    ) {
        Text("Logout")
    }

}