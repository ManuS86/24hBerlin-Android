package com.example.a24hberlin.ui.screens.components.utilityelements

import android.view.SoundEffectConstants
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.smallPadding

@Composable
fun <T> FilterDropdown(
    label: String,
    selectedValue: T?,
    onValueSelected: (T?) -> Unit,
    options: List<String>,
    stringToItem: (String) -> T?,
    itemToLabel: (T?) -> String?
) {
    val view = LocalView.current
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(bottom = smallPadding)
    ) {
        OutlinedButton(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(
                1.dp, if (selectedValue != null) Color.White.copy(
                    0.8f
                ) else Color.White.copy(
                    0.4f
                )
            ),
            shape = RoundedCornerShape(mediumRounding),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White,
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 6.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedValue?.let { itemToLabel(it) } ?: label,
                    color = if (selectedValue != null) Color.White else Color.White.copy(
                        0.6f
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.White
                )
            }
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
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onValueSelected(null)
                    isExpanded = !isExpanded
                }
            )

            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(end = mediumPadding)
                        )
                    },
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onValueSelected(stringToItem(option))
                        isExpanded = !isExpanded
                    }
                )
            }
        }
    }
}