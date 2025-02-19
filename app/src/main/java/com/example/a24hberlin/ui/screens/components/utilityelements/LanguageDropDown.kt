package com.example.a24hberlin.ui.screens.components.utilityelements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.utils.mediumPadding

@Composable
fun LanguageDropdown(
    label: String,
    selectedValue: Language?,
    onValueSelected: (Language?) -> Unit,
    options: List<Language>
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clickable { isExpanded = !isExpanded }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedValue?.resource?.let { stringResource(it) } ?: label,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Dropdown",
                tint = Color.Gray
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = !isExpanded },
            containerColor = Color.White
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = label,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = mediumPadding)
                    )
                },
                onClick = {
                    onValueSelected(null)
                    isExpanded = !isExpanded
                }
            )

            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(option.resource),
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(end = mediumPadding)
                        )
                    },
                    onClick = {
                        onValueSelected(option)
                        isExpanded = !isExpanded
                    }
                )
            }
        }
    }
}