package com.fithlanz.fithlanz.ui.theme

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



var fithlanztheme= lightColorScheme(
    primary = Orange,
    secondary = Color(0xFF0A84FF),
    background = Color(0xFFF5F5F5), // Blanco roto
    surface = Color.White,
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.Gray,
    onBackground = Color.White,
    onSurface = Color.Black,
    onError = Color.White,
)

var DarkFithlanzTheme = darkColorScheme(
    primary = Orange, // Naranja oscuro
    secondary = Color(0xFF0A84FF), // Azul oscuro
    background = Color.Black, // Fondo negro
    surface = Color.DarkGray, // Superficie gris oscuro
    error = Color(0xFFB00020), // Rojo oscuro para errores
    onPrimary = Color.White, // Texto blanco sobre el color primario
    onSecondary = Color.Gray, // Texto blanco sobre el color secundario
    onBackground = Color.White, // Texto blanco sobre el fondo negro
    onSurface = Color.White, // Texto blanco sobre la superficie gris oscuro
    onError = Color.White // Texto blanco sobre el color de error oscuro
)



@Composable
fun FithlanzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkFithlanzTheme else fithlanztheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

