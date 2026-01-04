package com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.theme.OffWhite
import com.esutor.twentyfourhoursberlin.ui.theme.Party
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.mediumRounding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding

@Composable
fun LocationCard(
    name: String?,
    address: String?
) {
    name?.let {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = mediumRounding,
            colors = cardColors(
                containerColor = OffWhite
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(standardPadding)
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.padding(end = smallPadding),
                    tint = Party
                )

                Column {
                    Text(
                        text = stringResource(R.string.location),
                        modifier = Modifier.padding(bottom = smallPadding),
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