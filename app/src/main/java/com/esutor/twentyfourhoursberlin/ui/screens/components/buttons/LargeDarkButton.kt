package com.esutor.twentyfourhoursberlin.ui.screens.components.buttons

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
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import com.esutor.twentyfourhoursberlin.ui.theme.circle
import com.esutor.twentyfourhoursberlin.ui.theme.largePadding
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding

@Composable
fun LargeDarkButton(
    label: String,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White
        ),
        shape = circle,
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