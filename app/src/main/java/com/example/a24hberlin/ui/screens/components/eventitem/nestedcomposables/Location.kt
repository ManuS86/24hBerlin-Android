package com.example.a24hberlin.ui.screens.components.eventitem.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.R
import com.example.a24hberlin.ui.theme.mediumPadding

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
                    contentDescription = stringResource(R.string.location),
                    modifier = Modifier
                        .padding(end = mediumPadding)
                        .size(20.dp)
                )

                Column {
                    Text(
                        text = name.trim().replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase() else it.toString()
                        },
                        overflow = Ellipsis,
                        maxLines = 1,
                        style = typography.bodyMedium
                    )

                    address?.let { address ->
                        address.split(", ").forEach { part ->
                            Text(
                                text = part,
                                overflow = Ellipsis,
                                maxLines = 1,
                                style = typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}