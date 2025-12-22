package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding

@Composable
fun SettingsButton(
    label: String,
    fontWeight: FontWeight,
    textAlign: TextAlign,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Button(
        onClick = {
            haptic.performHapticFeedback(TextHandleMove)
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = slightRounding,
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = slightRounding,
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black
        ),
        contentPadding = PaddingValues(regularPadding)
    ) {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = fontWeight,
            style = typography.bodyLarge,
            textAlign = textAlign
        )
    }
}
