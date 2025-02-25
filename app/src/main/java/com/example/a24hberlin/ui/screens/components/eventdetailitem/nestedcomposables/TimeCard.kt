package com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.Details
import com.example.a24hberlin.ui.theme.Party
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.regularPadding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimeCard(start: LocalDateTime, end: LocalDateTime?) {
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(mediumRounding),
        colors = CardDefaults.cardColors(
            containerColor = Details
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(regularPadding)
        ) {
            Icon(
                Icons.Default.WatchLater,
                contentDescription = null,
                Modifier.padding(end = mediumPadding),
                tint = Party
            )

            SelectionContainer {
                Column {
                    Text(
                        stringResource(R.string.time),
                        Modifier.padding(bottom = mediumPadding),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        Text(
                            start.format(
                                DateTimeFormatter.ofPattern(
                                    "d MMM yyyy",
                                    Locale.getDefault()
                                )
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.DarkGray
                        )

                        end?.let {
                            Text(
                                " - ",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.DarkGray
                            )

                            Text(
                                end.format(
                                    DateTimeFormatter.ofPattern(
                                        "d MMM yyyy",
                                        Locale.getDefault()
                                    )
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}