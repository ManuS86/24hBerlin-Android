package com.example.a24hberlin.ui.screens.components.event.detailitem.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.Details
import com.example.a24hberlin.ui.theme.Party
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.mediumRounding
import com.example.a24hberlin.ui.theme.regularPadding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale.getDefault

@Composable
fun TimeCard(start: LocalDateTime, end: LocalDateTime?) {
    val locale = getDefault()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = mediumRounding,
        colors = cardColors(
            containerColor = Details
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(regularPadding)
        ) {
            Icon(
                imageVector = Default.WatchLater,
                contentDescription = null,
                modifier = Modifier.padding(end = halfPadding),
                tint = Party
            )

            SelectionContainer {
                Column {
                    Text(
                        text = stringResource(R.string.time),
                        modifier = Modifier.padding(bottom = halfPadding),
                        style = typography.titleMedium,
                        fontWeight = Bold
                    )

                    Column(horizontalAlignment = CenterHorizontally) {
                        Text(
                            text = start.format(
                                DateTimeFormatter.ofPattern(
                                    "MMMM d, yyyy HH:mm",
                                    locale
                                )
                            ),
                            style = typography.bodyLarge,
                            color = DarkGray
                        )

                        end?.let {
                            Text(
                                text = " - ",
                                style = typography.bodyLarge,
                                color = DarkGray
                            )

                            Text(
                                text = end.format(
                                    DateTimeFormatter.ofPattern(
                                        "MMMM d, yyyy HH:mm",
                                        locale
                                    )
                                ),
                                style = typography.bodyLarge,
                                color = DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}