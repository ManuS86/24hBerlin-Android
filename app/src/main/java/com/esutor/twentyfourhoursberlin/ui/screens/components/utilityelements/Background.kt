package com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.res.painterResource
import com.esutor.twentyfourhoursberlin.R

@Composable
fun Background() {
    Image(
        painter = painterResource(R.drawable.background),
        contentDescription = null,
        contentScale = FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}