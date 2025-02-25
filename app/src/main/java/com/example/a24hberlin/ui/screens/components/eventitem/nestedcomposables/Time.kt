package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.R
import com.example.a24hberlin.utils.mediumPadding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Time(
    start: LocalDateTime,
    end: LocalDateTime?
) {
    Row {
        Icon(
            imageVector = Icons.Filled.WatchLater,
            contentDescription = stringResource(R.string.time),
            modifier = Modifier
                .padding(end = mediumPadding)
                .size(18.dp)
        )

        Text(
            start.format(
                DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
            ),
            style = MaterialTheme.typography.bodyMedium
        )

        end?.let { endTime ->
            Text(
                " - ",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                endTime.format(
                    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}