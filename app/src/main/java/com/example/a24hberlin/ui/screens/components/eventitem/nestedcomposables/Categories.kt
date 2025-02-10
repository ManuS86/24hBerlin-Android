package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle

@Composable
fun Categories(
    eventType: Map<String, String>?,
    sounds: Map<String, String>?
) {
    eventType?.let {
        Row {
            Text(
                "Types",
                fontStyle = FontStyle.Italic,
                color = Color.White.copy(0.8f)
            )
            Text(eventType.values.joinToString(", "))
        }
    }

    sounds?.let {
        Row {
            Text(
                "Sounds",
                fontStyle = FontStyle.Italic,
                color = Color.White.copy(0.8f)
            )
            Text(sounds.values.joinToString(", "))
        }
    }
}