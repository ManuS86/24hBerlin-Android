package com.example.a24hberlin.ui.screens.components.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.res.painterResource
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.logoSize

@Composable
fun AppLogo() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "App Logo",
            contentScale = FillBounds,
            modifier = Modifier.size(logoSize)
        )
    }
}