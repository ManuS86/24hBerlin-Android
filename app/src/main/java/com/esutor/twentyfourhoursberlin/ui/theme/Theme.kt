package com.esutor.twentyfourhoursberlin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White

private val LightColorScheme = lightColorScheme(
    primary = Black,
    secondary = White,
    tertiary = Gray,
    background = White,
    surface = Black,
    surfaceVariant = Details,
    primaryContainer = Black,
    onBackground = TextOffBlack,
    onSurface = TextOffBlack,
    onSurfaceVariant = TextOffBlack,
    onPrimary = White,
    onSecondary = TextOffBlack,
    onTertiary = White
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}