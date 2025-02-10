package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.utils.mediumPadding

@Composable
fun Location(
    locationName: String?,
    address: String?
) {
    Row {
        locationName?.let { name ->
            Row {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.padding(end = mediumPadding)
                )

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = name,
                        modifier = Modifier.padding(top = 3.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    address?.let { address ->
                        address.split(", ").forEach { part ->
                            Text(
                                text = part,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}