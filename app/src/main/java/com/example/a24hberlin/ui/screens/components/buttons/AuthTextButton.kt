package com.example.a24hberlin.ui.screens.components.buttons

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight.Companion.Bold

@Composable
fun AuthTextButton(
    label: String,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    TextButton(
        {
            haptic.performHapticFeedback(TextHandleMove)
            onClick()
        }
    ) {
        Text(
            text = label,
            fontWeight = Bold,
            color = Black,
            style = typography.bodyLarge
        )
    }
}