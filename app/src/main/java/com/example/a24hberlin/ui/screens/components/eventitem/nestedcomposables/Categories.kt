package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import com.example.a24hberlin.R
import com.example.a24hberlin.utils.mediumPadding

@Composable
fun Categories(
    eventType: Map<String, String>?,
    sounds: Map<String, String>?
) {
    eventType?.let {
        Column(verticalArrangement = Arrangement.spacedBy(mediumPadding)) {
            Row {
                Text(
                    text = stringResource(R.string.types),
                    modifier = Modifier.padding(end = mediumPadding),
                    fontStyle = FontStyle.Italic,
                    color = Color.White.copy(0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = eventType.values.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            sounds?.let {
                Row {
                    Text(
                        text = stringResource(R.string.sounds),
                        modifier = Modifier.padding(end = mediumPadding),
                        fontStyle = FontStyle.Italic,
                        color = Color.White.copy(0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = sounds.values.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}