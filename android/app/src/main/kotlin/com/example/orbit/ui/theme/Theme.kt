package com.example.orbit.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

private val DarkColorScheme = darkColorScheme(
    primary = OrbitAccent,
    secondary = OrbitAccentSecondary,
    background = OrbitBackgroundDark,
    surface = OrbitSurface,
    surfaceVariant = OrbitSurfaceVariant,
    onPrimary = Color.Black,
    onBackground = OrbitTextPrimary,
    onSurface = OrbitTextPrimary,
    onSurfaceVariant = OrbitTextPrimary
)

@Composable
fun OrbitTheme(
    darkTheme: Boolean = true, // Forced Dark Theme per requirements
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(), // Assuming default Typography in Type.kt, but we will use specific thin weights in components
        content = content
    )
}
