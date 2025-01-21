package com.example.to_dolistapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AutoAwesomeMotion
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.to_dolistapp.presentation.screen.AuthScreen
import com.example.to_dolistapp.presentation.screen.BrowseScreen
import com.example.to_dolistapp.presentation.screen.LoginScreen
import com.example.to_dolistapp.presentation.screen.SearchScreen
import com.example.to_dolistapp.presentation.screen.SignupScreen
import com.example.to_dolistapp.presentation.screen.ToDoScreen
import com.example.to_dolistapp.presentation.screen.UpcomingScreen
import com.example.to_dolistapp.ui.theme.ToDoListAppTheme
import com.example.to_dolistapp.viewmodel.AuthViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInLauncher: androidx.activity.result.ActivityResultLauncher<Intent>
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize CallbackManager for Facebook
        callbackManager = CallbackManager.Factory.create()

        // Initialize Google Sign-In Launcher
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            authViewModel.handleGoogleSignInResult(data)
        }

        setContent {
            ToDoListAppTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route
                        if (currentRoute in listOf("todo", "upcoming", "search", "browse")) {
                            CustomTopAppBar(currentRoute = currentRoute ?: "") { action ->
                                // Handle menu clicks here if needed
                            }
                        }
                    },
                    bottomBar = {
                        val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route
                        if (currentRoute in listOf("todo", "upcoming", "search", "browse")) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        authViewModel = authViewModel,
                        navController = navController,
                        innerPadding = innerPadding
                    ) { intent ->
                        googleSignInLauncher.launch(intent)
                    }
                }
            }
        }

        // Register Facebook Login Callback
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    authViewModel.handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    authViewModel.updateErrorMessage("Facebook Login Cancelled")
                }

                override fun onError(error: FacebookException) {
                    authViewModel.updateErrorMessage("Facebook Login Failed: ${error.message}")
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun startFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("email", "public_profile")
        )
    }
}


@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues,
    startActivityForResult: (Intent) -> Unit
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            if (navController.currentDestination?.route != "todo") {
                navController.navigate("todo") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        } else {
            if (navController.currentDestination?.route !in listOf("auth", "login", "signup")) {
                navController.navigate("auth") {
                    popUpTo("todo") { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "auth",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("auth") {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel,
                startActivityForResult = startActivityForResult
            )
        }
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("signup") {
            SignupScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("todo") {
            ToDoScreen(navController = navController)
        }
        composable("upcoming") {
            UpcomingScreen(navController = navController)
        }
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable("browse") {
            BrowseScreen(navController = navController)
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentDestination by navController.currentBackStackEntryAsState()

    Column {
        // Add the top border
        HorizontalDivider(
            color = Color.DarkGray,
            thickness = 0.5.dp,
            modifier = Modifier.fillMaxWidth()
        )

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background
        ) {
            val items = listOf(
                BottomNavItem("todo", Icons.Filled.Home, Icons.Outlined.Home, "To-Do"),
                BottomNavItem("upcoming", Icons.Filled.Event, Icons.Outlined.Event, "Upcoming"),
                BottomNavItem("search", Icons.Filled.Search, Icons.Outlined.Search, "Search"),
                BottomNavItem("browse", Icons.Filled.Explore, Icons.Outlined.Explore, "Browse")
            )

            items.forEach { item ->
                val isSelected = currentDestination?.destination?.hierarchy?.any {
                    it.route == item.route
                } == true

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                            contentDescription = item.label
                        )
                    },
                    label = { Text(item.label) },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }
    }
}



data class BottomNavItem(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(currentRoute: String, onMenuClick: (String) -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = when (currentRoute) {
                    "todo" -> "Today"
                    "upcoming" -> "Upcoming"
                    "search" -> "Search"
                    "browse" -> "xyz"
                    else -> ""
                }
            )
        },
        actions = {
            when (currentRoute) {
                "todo", "upcoming" -> {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        offset = DpOffset(x = 0.dp, y = (-48).dp), // Adjusted to start from the top right corner
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = MenuDefaults.ShadowElevation,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            )
                            .width(200.dp)
                    ) {
                        DropdownMenuItem(
                            onClick = { onMenuClick("view"); showMenu = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.ViewAgenda,
                                    contentDescription = "View"
                                )
                            },
                            text = { Text("View") }
                        )
                        DropdownMenuItem(
                            onClick = { onMenuClick("select_tasks"); showMenu = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.AutoAwesomeMotion,
                                    contentDescription = "Select Tasks"
                                )
                            },
                            text = { Text("Select tasks") }
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            thickness = 0.8.dp
                        )
                        DropdownMenuItem(
                            onClick = { onMenuClick("activity_log"); showMenu = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.History,
                                    contentDescription = "Activity Log"
                                )
                            },
                            text = { Text("Activity log") }
                        )
                    }
                }
                "search" -> {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        offset = DpOffset(x = 0.dp, y = (-48).dp), // Adjusted to start from the top right corner
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = MenuDefaults.ShadowElevation,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            )
                            .width(200.dp)
                    ) {
                        DropdownMenuItem(
                            onClick = { onMenuClick("activity_log"); showMenu = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.History,
                                    contentDescription = "Activity Log"
                                )
                            },
                            text = { Text("Activity log") }
                        )
                    }
                }
                "browse" -> {
                    IconButton(onClick = { onMenuClick("notifications") }) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                    IconButton(onClick = { onMenuClick("settings") }) {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}
