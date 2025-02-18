package com.example.a24hberlin.ui.screens.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.a24hberlin.utils.largePadding
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.slightRounding

@Composable
fun LargeDarkButton(
    label: String,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(slightRounding),
        modifier = Modifier
            .padding(top = largePadding)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(mediumPadding),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}