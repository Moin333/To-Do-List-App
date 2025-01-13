package com.example.to_dolistapp.presentation.screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.to_dolistapp.MainActivity
import com.example.to_dolistapp.viewmodel.AuthViewModel
import com.example.to_dolistapp.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult


@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    startActivityForResult: (Intent) -> Unit
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val errorMessage by authViewModel.errorMessage.collectAsStateWithLifecycle()

    errorMessage?.let {
        Text(text = it, color = Color.Red)
    }

    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            if (navController.currentDestination?.route != "todo") {
                navController.navigate("todo") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        } else {
            if (navController.currentDestination?.route != "auth") {
                navController.navigate("auth") {
                    popUpTo("todo") { inclusive = true }
                }
            }
        }
    }

    val context = LocalContext.current as MainActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher),
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "todolist",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(R.drawable.todo_illustration),
            contentDescription = "illustration",
            modifier = Modifier.size(320.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Organize your\n work and life, finally.",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                val signInIntent = authViewModel.getGoogleSignInIntent()
                startActivityForResult(signInIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Continue with Google",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
                )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                context.startFacebookLogin()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Continue with Facebook",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Continue with more options",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val uriHandler = LocalUriHandler.current
            val annotatedString = buildAnnotatedString {
                append("By continuing with the services above, you agree to Todolist's ")

                pushStringAnnotation(tag = "terms", annotation = "https://www.termsfeed.com/live/4a1529e6-61f0-454d-800c-fe595197ca1e")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline)) {
                    append("Terms of Service")
                }
                pop()

                append(" and ")

                pushStringAnnotation(tag = "privacy", annotation = "https://www.termsfeed.com/live/4a1529e6-61f0-454d-800c-fe595197ca1e")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline)) {
                    append("Privacy Policy")
                }
                pop()

                append(".")
            }

            var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            textLayoutResult?.let { layoutResult ->
                                val position = layoutResult.getOffsetForPosition(offset)
                                annotatedString.getStringAnnotations(position, position)
                                    .firstOrNull()?.let { annotation ->
                                        uriHandler.openUri(annotation.item)
                                    }
                            }
                        }
                    },
                onTextLayout = { layoutResult ->
                    textLayoutResult = layoutResult
                }
            )
        }


        authState.errorMessage?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }
    }
}
