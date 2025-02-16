package com.example.a24hberlin.ui.screens.components.utilitybars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.enums.Sound
import com.example.a24hberlin.ui.screens.components.utilityelements.FilterDropdown
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = regularPadding),
                horizontalArrangement = Arrangement.spacedBy(mediumPadding),
            ) {
                FilterDropdown(
                    label = "Type",
                    selectedValue = selectedEventType,
                    onValueSelected = onEventTypeSelected,
                    options = EventType.allValues.map { it.label },
                    stringToItem = { str -> EventType.entries.firstOrNull { it.label == str } },
                    itemToLabel = { eventType -> eventType?.label }
                )

                FilterDropdown(
                    label = "Sound",
                    selectedValue = selectedSound,
                    onValueSelected = onSoundSelected,
                    options = Sound.allValues.map { it.label },
                    stringToItem = { str -> Sound.entries.firstOrNull { it.label == str } },
                    itemToLabel = { sound -> sound?.label }
                )

                FilterDropdown(
                    label = "Venue",
                    selectedValue = selectedVenue,
                    onValueSelected = onVenueSelected,
                    options = venues,
                    stringToItem = { it },
                    itemToLabel = { venue -> venue }
                )

                if (selectedEventType != null || selectedSound != null || selectedVenue != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear Filters",
                            Modifier
                                .padding(top = 12.dp)
                                .clickable {
                                    onEventTypeSelected(null)
                                    onSoundSelected(null)
                                    onVenueSelected(null)
                                },
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}