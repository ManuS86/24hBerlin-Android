package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding

@Composable
fun SettingsButton(
    label: String,
    fontWeight: FontWeight,
    textAlign: TextAlign,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .expressivePop(interactionSource)
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
        contentPadding = PaddingValues(standardPadding),
        interactionSource = interactionSource
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
