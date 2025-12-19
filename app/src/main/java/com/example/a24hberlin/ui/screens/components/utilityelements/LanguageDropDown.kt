package com.example.a24hberlin.ui.screens.components.utilityelements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.ui.theme.mediumPadding

@Composable
fun LanguageDropdown(
    label: String,
    selectedValue: Language?,
    onValueSelected: (Language?) -> Unit,
    options: List<Language>
) {
    val haptic = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                role = Role.Button,
                onClick = { isExpanded = !isExpanded }
            )
    ) {
        Row(
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = selectedValue?.resource?.let { stringResource(it) } ?: label,
                color = Black,
                fontWeight = Medium,
                overflow = Ellipsis,
                maxLines = 1
            )

            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Dropdown",
                tint = Gray
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = !isExpanded },
            containerColor = White
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = label,
                        overflow = Ellipsis,
                        modifier = Modifier.padding(end = mediumPadding)
                    )
                },
                onClick = {
                    haptic.performHapticFeedback(TextHandleMove)
                    onValueSelected(null)
                    isExpanded = !isExpanded
                }
            )

            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(option.resource),
                            overflow = Ellipsis,
                            modifier = Modifier
                                .padding(end = mediumPadding)
                        )
                    },
                    onClick = {
                        haptic.performHapticFeedback(TextHandleMove)
                        onValueSelected(option)
                        isExpanded = !isExpanded
                    }
                )
            }
        }
    }
}