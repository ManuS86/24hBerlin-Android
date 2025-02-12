package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.smallPadding

@Composable
fun Categories(
    eventType: Map<String, String>?,
    sounds: Map<String, String>?
) {
    eventType?.let {
        Column(verticalArrangement = Arrangement.spacedBy(smallPadding)) {
            Row {
                Text(
                    "Types",
                    Modifier.padding(end = mediumPadding),
                    fontStyle = FontStyle.Italic,
                    color = Color.White.copy(0.8f)
                )
                Text(eventType.values.joinToString(", "))
            }

            sounds?.let {
                Row {
                    Text(
                        "Sounds",
                        Modifier.padding(end = mediumPadding),
                        fontStyle = FontStyle.Italic,
                        color = Color.White.copy(0.8f)
                    )
                    Text(sounds.values.joinToString(", "))
                }
            }
        }
    }
}