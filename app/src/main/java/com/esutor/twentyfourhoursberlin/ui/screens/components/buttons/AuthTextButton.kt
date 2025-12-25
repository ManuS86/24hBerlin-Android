package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold

@Composable
fun AuthTextButton(
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        { onClick() }
    ) {
        Text(
            text = label,
            fontWeight = Bold,
            color = Black,
            style = typography.bodyLarge
        )
    }
}