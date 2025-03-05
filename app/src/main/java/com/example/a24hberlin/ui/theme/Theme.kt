package com.example.a24hberlin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    secondary = Color.White,
    tertiary = Color.Gray,
    background = Color.White,
    surface = Color.Black,
    surfaceVariant = Details,
    primaryContainer = Color.Black,
    onBackground = TextOffBlack,
    onSurface = TextOffBlack,
    onSurfaceVariant = TextOffBlack

    /* Other default colors to override
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    */
)

@Composable
fun _24hBerlinTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}