package com.example.to_dolistapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import com.example.to_dolistapp.ui.theme.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.to_dolistapp.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLogin by rememberSaveable { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isLogin) "Login" else "Signup",
            style = Typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (isLogin) {
                    authViewModel.login(email, password) { success, error ->
                        if (success) {
                            navController.navigate("todo") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            errorMessage = error ?: "Login failed"
                        }
                    }
                } else {
                    authViewModel.register(email, password) { success, error ->
                        if (success) {
                            navController.navigate("todo") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            errorMessage = error ?: "Signup failed"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLogin) "Login" else "Signup")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { isLogin = !isLogin },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLogin) "Don't have an account? Signup" else "Already have an account? Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
    }
}
