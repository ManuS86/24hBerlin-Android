package com.example.a24hberlin.ui.screens.components.utilitybars

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.enums.Sound
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.regularPadding
import com.example.a24hberlin.utils.smallPadding

@Composable
fun FilterBar(
    selectedEventType: EventType?,
    onEventTypeSelected: (EventType?) -> Unit,
    selectedMonth: Month?,
    onMonthSelected: (Month?) -> Unit,
    selectedSound: Sound?,
    onSoundSelected: (Sound?) -> Unit,
    selectedVenue: String?,
    onVenueSelected: (String?) -> Unit,
    venues: List<String>
) {
    var showFilters by remember { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(bottom = smallPadding)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = regularPadding)
                .padding(bottom = smallPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(mediumPadding)
            ) {
                Button(
                    onClick = { onMonthSelected(null) },
                    shape = RoundedCornerShape(mediumRounding),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedMonth == null) Color.DarkGray.copy(
                            0.8f
                        ) else Color.DarkGray.copy(
                            0.4f
                        )
                    )
                ) {
                    Text(
                        text = "All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedMonth == null) Color.White else Color.Gray
                    )
                }

                Month.allValues.forEach { month ->
                    Button(
                        onClick = { onMonthSelected(month) },
                        shape = RoundedCornerShape(mediumRounding),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMonth == month) Color.DarkGray.copy(
                                0.8f
                            ) else Color.DarkGray.copy(
                                0.4f
                            )
                        )
                    ) {
                        Text(
                            text = month.englishName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selectedMonth == month) Color.White else Color.White.copy(
                                0.6f
                            )
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Show Filters",
                Modifier
                    .padding(start = mediumPadding)
                    .clickable { showFilters = !showFilters },
                tint = Color.White
            )
        }


        if (showFilters) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                FilterDropdown(
                    label = "Type",
                    selectedValue = selectedEventType,
                    onValueSelected = onEventTypeSelected,
                    options = EventType.allValues.map { it.label }
                )
//                FilterDropdown(
//                    label = "Sound",
//                    selectedValue = selectedSound,
//                    onValueSelected = onSoundSelected,
//                    options = Sound.allValues.map { it.label }
//                )
//
//                FilterDropdown(
//                    label = "Venue",
//                    selectedValue = selectedVenue,
//                    onValueSelected = onVenueSelected,
//                    options = venues
//                )

                if (selectedEventType != null || selectedSound != null || selectedVenue != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            onEventTypeSelected(null)
                            onSoundSelected(null)
                            onVenueSelected(null)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear Filters",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FilterDropdown(
    label: String,
    selectedValue: EventType?,
    onValueSelected: (EventType?) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(bottom = smallPadding)
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(mediumRounding),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White,
                containerColor = Color.Transparent
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedValue?.toString() ?: label,
                    color = if (selectedValue == null) Color.Gray else Color.White
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.White
                )
            }
        }

//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            DropdownMenuItem(
//                text = {  }
//                , onClick = {
//                onValueSelected(null)
//                expanded = false
//            }) {
//                Text(text = label)
//            }
//            options.forEach { option ->
//                DropdownMenuItem(onClick = {
//                    onValueSelected(
//                        when (label) {
//                            "Type" -> EventType.values().firstOrNull { it.label == option }
//                            "Sound" -> Sound.values().firstOrNull { it.label == option }
//                            "Venue" -> option
//                            else -> null
//                        }
//                    )
//                    expanded = false
//                }) {
//                    Text(text = option)
//                }
//            }
//        }
    }
}