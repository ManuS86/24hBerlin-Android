package com.example.a24hberlin.ui.screens.components.event.detailitem.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.Details
import com.example.a24hberlin.ui.theme.Party
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.mediumRounding
import com.example.a24hberlin.ui.theme.regularPadding

@Composable
fun LocationCard(
    name: String?,
    address: String?
) {
    name?.let {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(mediumRounding),
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
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.padding(end = halfPadding),
                    tint = Party
                )

                Column {
                    Text(
                        text = stringResource(R.string.location),
                        modifier = Modifier.padding(bottom = halfPadding),
                        style = typography.titleMedium,
                        fontWeight = Bold
                    )

                    SelectionContainer {
                        Column {
                            Text(
                                text = name,
                                overflow = Ellipsis,
                                maxLines = 1,
                                style = typography.bodyLarge,
                                color = DarkGray
                            )

                            address?.let {
                                address.split(", ").forEach { part ->
                                    Text(
                                        text = part,
                                        overflow = Ellipsis,
                                        maxLines = 1,
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
    }
}