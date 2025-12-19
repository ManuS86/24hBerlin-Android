package com.example.a24hberlin.ui.screens.components.utilityelements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.slightRounding
import com.example.a24hberlin.ui.theme.microPadding

@Composable
fun <T> FilterDropdown(
    label: String,
    selectedValue: T?,
    onValueSelected: (T?) -> Unit,
    options: List<String>,
    stringToItem: (String) -> T?,
    itemToLabel: (T?) -> String?
) {
    val haptic = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }

    val arrowAlpha = if (selectedValue != null) 1f else 0.8f
    val contentAlpha = if (selectedValue != null) 1f else 0.5f

    Column(
        modifier = Modifier
            .padding(top = microPadding)
            .padding(bottom = halfPadding)
    ) {
        OutlinedButton(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.fillMaxWidth()
                    .height(36.dp),
            border = BorderStroke(1.dp, White.copy(contentAlpha)),
            shape = RoundedCornerShape(slightRounding),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = White.copy(contentAlpha),
                containerColor = Transparent
            ),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 6.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                Text(
                    text = selectedValue?.let { itemToLabel(it) } ?: label,
                    color = White.copy(contentAlpha),
                    overflow = Ellipsis,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = White.copy(arrowAlpha)
                )
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            containerColor = White
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = label,
                        overflow = Ellipsis,
                        modifier = Modifier.padding(end = halfPadding)
                    )
                },
                onClick = {
                    haptic.performHapticFeedback(TextHandleMove)
                    onValueSelected(null)
                    isExpanded = false
                }
            )

            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            overflow = Ellipsis,
                            modifier = Modifier
                                .padding(end = halfPadding)
                        )
                    },
                    onClick = {
                        haptic.performHapticFeedback(TextHandleMove)
                        onValueSelected(stringToItem(option))
                        isExpanded = false
                    }
                )
            }
        }
    }
}