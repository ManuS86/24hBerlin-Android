package com.example.a24hberlin.ui.screens.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.utils.regularPadding
import com.example.a24hberlin.utils.slightRounding

@Composable
fun SettingsButton(
    label: String,
    fontWeight: FontWeight,
    textAlign: TextAlign,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(slightRounding),
                ambientColor = Color.Gray.copy(0.5f),
                spotColor = Color.Gray.copy(0.5f)
            ),
        shape = RoundedCornerShape(slightRounding),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(regularPadding)
    ) {
        Text(
            label,
            Modifier.fillMaxWidth(),
            fontWeight = fontWeight,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = textAlign
        )
    }
}
