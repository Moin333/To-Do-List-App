package com.example.to_dolistapp.presentation.screen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.to_dolistapp.MainActivity
import com.example.to_dolistapp.viewmodel.AuthViewModel
import com.example.to_dolistapp.ui.theme.Typography

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    startActivityForResult: (Intent) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLogin by rememberSaveable { mutableStateOf(true) }

    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    val errorMessage by authViewModel.errorMessage.collectAsStateWithLifecycle()

    errorMessage?.let {
        Text(text = it, color = Color.Red)
    }

    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            if (navController.currentDestination?.route != "todo") {
                navController.navigate("todo") {
                    popUpTo("login") { inclusive = true }
                }
            }
        } else {
            if (navController.currentDestination?.route != "login") {
                navController.navigate("login") {
                    popUpTo("todo") { inclusive = true }
                }
            }
        }
    }

    val context = LocalContext.current as MainActivity

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
                    authViewModel.login(email, password)
                } else {
                    authViewModel.register(email, password)
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
        Button(
            onClick = {
                val signInIntent = authViewModel.getGoogleSignInIntent()
                startActivityForResult(signInIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign in with Google")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                context.startFacebookLogin()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign in with Facebook")
        }
        Spacer(modifier = Modifier.height(8.dp))
        authState.errorMessage?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }
    }
}
