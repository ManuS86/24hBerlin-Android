package com.example.a24hberlin.ui.screens.components.event.item.nestedcomposables

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.utils.cleanToAnnotatedString
import com.example.a24hberlin.ui.theme.halfPadding

@Composable
fun Categories(
    eventType: Map<String, String>?,
    sounds: Map<String, String>?
) {
    val context = LocalContext.current
    val textColor = White.copy(0.8f)

    Column(verticalArrangement = spacedBy(halfPadding)) {
    eventType?.let { type ->
        val translatedAndCleanedTypes = remember(type) {
            eventType.values.joinToString(", ") { rawValue ->
                val cleaned = rawValue.cleanToAnnotatedString().text
                EventType.fromString(cleaned)?.let {
                    context.getString(it.labelRes)
                } ?: cleaned
            }
        }

            Row {
                Text(
                    text = stringResource(R.string.type),
                    modifier = Modifier.padding(end = halfPadding),
                    fontStyle = Italic,
                    color = textColor,
                    style = typography.bodyMedium
                )

                Text(
                    text = translatedAndCleanedTypes,
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
                    text = stringResource(R.string.music),
                    modifier = Modifier.padding(end = halfPadding),
                    fontStyle = Italic,
                    color = textColor,
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