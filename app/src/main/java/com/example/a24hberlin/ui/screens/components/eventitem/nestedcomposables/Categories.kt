package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import com.example.a24hberlin.R
import com.example.a24hberlin.utils.cleanToAnnotatedString
import com.example.a24hberlin.ui.theme.mediumPadding

@Composable
fun Categories(
    eventType: Map<String, String>?,
    sounds: Map<String, String>?
) {
    Column(verticalArrangement = Arrangement.spacedBy(mediumPadding)) {

    eventType?.let {
        val cleanedEventTypes = remember(it) {
            it.values.joinToString(", ").cleanToAnnotatedString()
        }

            Row {
                Text(
                    text = stringResource(R.string.types),
                    modifier = Modifier.padding(end = mediumPadding),
                    fontStyle = Italic,
                    color = White.copy(0.8f),
                    style = typography.bodyMedium
                )

                Text(
                    text = cleanedEventTypes,
                    style = typography.bodyMedium
                )
            }
        }

        sounds?.let {
            val joinedSounds = remember(it) {
                it.values.joinToString(", ")
            }

            Row {
                Text(
                    text = stringResource(R.string.sounds),
                    modifier = Modifier.padding(end = mediumPadding),
                    fontStyle = Italic,
                    color = White.copy(0.8f),
                    style = typography.bodyMedium
                )

                Text(
                    text = joinedSounds,
                    style = typography.bodyMedium
                )
            }
        }
    }
}