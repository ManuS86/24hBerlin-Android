package com.esutor.twentyfourhoursberlin.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SetSystemBarColorsToLight(value: Boolean) {
    val context = LocalContext.current
    val view = LocalView.current

    SideEffect {
        val window = (context as? Activity)?.window ?: return@SideEffect
        val controller = WindowInsetsControllerCompat(window, view)

        controller.isAppearanceLightStatusBars = value
        controller.isAppearanceLightNavigationBars = false
    }
}