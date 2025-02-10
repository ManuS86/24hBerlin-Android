package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.a24hberlin.utils.mediumPadding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Time(
    start: LocalDateTime,
    end: LocalDateTime?
) {
    val locale = Locale.getDefault()

    Row {
        Icon(
            imageVector = Icons.Filled.WatchLater,
            contentDescription = "Location",
            modifier = Modifier.padding(end = mediumPadding)
        )
        Text(
            text = start.format(
                DateTimeFormatter.ofPattern("HH:mm").withLocale(locale)
            )
        )
        end?.let { endTime ->
            Text(" - ")
            Text(
                text = endTime.format(
                    DateTimeFormatter.ofPattern("HH:mm").withLocale(locale)
                )
            )
        }
    }
}