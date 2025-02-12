package com.example.to_dolistapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark and Light Color Schemes
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark,
    onPrimaryContainer = OnPrimaryContainer,
    background = Color(0xFF1F1F1F),     // Lighter dark background
    surface = Color(0xFF242424),        // Slightly lighter surface
    surfaceVariant = Color(0xFF2D2D2D), // For cards and elevated surfaces
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceTint = Color(0xFF262626),    // Darker shade for bottom navigation
    outlineVariant = Color(0xFF3D3D3D)  // For dividers and borders
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    tertiary = TertiaryLight,
    onPrimaryContainer = OnPrimaryContainer,
    background = Color(0xFFFFFFFF),
    surface = Color.White,
    surfaceVariant = Color(0xFFF3F3F3),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    surfaceTint = Color(0xFFFCFCF7),
    outlineVariant = Color(0xFFE0E0E0)
)

@Composable
fun ToDoListAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Control system bars using insets and decor fits
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme // Light icons for dark mode
                isAppearanceLightNavigationBars = !darkTheme // Light icons for navigation bar
            }

            // Set the status and navigation bar background colors
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surfaceTint.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
