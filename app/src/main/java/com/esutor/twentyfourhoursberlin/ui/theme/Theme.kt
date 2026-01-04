package com.esutor.twentyfourhoursberlin.ui.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
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
    surfaceVariant = OffWhite,
    primaryContainer = Black,
    onBackground = OffBlack,
    onSurface = OffBlack,
    onSurfaceVariant = OffBlack,
    onPrimary = White,
    onSecondary = OffBlack,
    onTertiary = White,
    outline = Black,
    surfaceContainerHighest = White
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialExpressiveTheme(
        colorScheme = LightColorScheme,
        motionScheme = MotionScheme.expressive(),
        typography = Typography,
        content = content
    )
}