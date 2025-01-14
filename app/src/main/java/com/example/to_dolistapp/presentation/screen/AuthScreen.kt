package com.example.to_dolistapp.presentation.screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.DpOffset


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
            modifier = Modifier.fillMaxWidth().height(46.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Continue with Google",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                context.startFacebookLogin()
            },
            modifier = Modifier.fillMaxWidth().height(46.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(R.drawable.facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Continue with Facebook",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }


        AuthOptionsMenu(
            onSignupWithEmailClick = {
                navController.navigate("signup")
            },
            onLoginWithEmailClick = {
                navController.navigate("login")
            }
        )


        Spacer(modifier = Modifier.height(4.dp))

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


@Composable
fun AuthOptionsMenu(
    onSignupWithEmailClick: () -> Unit,
    onLoginWithEmailClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // TextButton to open the DropdownMenu
        TextButton(
            onClick = { isMenuExpanded = !isMenuExpanded },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = "Continue with more options",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }


        // DropdownMenu directly anchored to the TextButton
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            modifier = Modifier
                .align(Alignment.Center)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(start = 21.dp, end = 21.dp),
            offset = DpOffset(x = 45.dp, y = 8.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.pencil),
                            contentDescription = "Sign up",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Sign up with Email", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                onClick = {
                    isMenuExpanded = false
                    onSignupWithEmailClick()
                }
            )

            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "Login",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Login with Email", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                onClick = {
                    isMenuExpanded = false
                    onLoginWithEmailClick()
                }
            )
        }
    }
}



