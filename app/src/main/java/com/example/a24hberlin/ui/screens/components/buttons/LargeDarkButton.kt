package com.example.a24hberlin.ui.screens.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import com.example.a24hberlin.ui.theme.largePadding
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.slightRounding

@Composable
fun LargeDarkButton(
    label: String,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    ElevatedButton(
        onClick = {
            haptic.performHapticFeedback(TextHandleMove)
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White
        ),
        shape = slightRounding,
        modifier = Modifier
            .padding(top = largePadding)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(halfPadding),
            fontWeight = SemiBold,
            style = typography.bodyLarge
        )
    }
}