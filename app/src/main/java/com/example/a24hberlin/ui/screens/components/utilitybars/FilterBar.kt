package com.example.a24hberlin.ui.screens.components.utilitybars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.ui.screens.components.utilityelements.FilterDropdown
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.ui.theme.mediumPadding
import com.example.a24hberlin.ui.theme.regularPadding
import com.example.a24hberlin.ui.theme.slightRounding
import com.example.a24hberlin.ui.theme.smallPadding

@Composable
fun FilterBar(
    selectedEventType: EventType?,
    onEventTypeSelected: (EventType?) -> Unit,
    selectedMonth: Month?,
    onMonthSelected: (Month?) -> Unit,
    selectedSound: String?,
    onSoundSelected: (String?) -> Unit,
    selectedVenue: String?,
    onVenueSelected: (String?) -> Unit,
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val eventVM: EventViewModel = viewModel()

    val horizontalScrollState = rememberScrollState()
    val horizontalScrollState2 = rememberScrollState()

    var showFilters by remember { mutableStateOf(false) }
    val uniqueLocations by eventVM.uniqueLocations.collectAsStateWithLifecycle()
    val uniqueSounds by eventVM.uniqueSounds.collectAsStateWithLifecycle()

    val filterAlpha = if (selectedMonth != null) 0.9f else 0.5f
    val textAlpha = if (selectedMonth != null) 1f else 0.6f

    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(bottom = smallPadding)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = regularPadding)
                .padding(bottom = mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(mediumPadding)
            ) {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(TextHandleMove)
                        onMonthSelected(null)
                    },
                    modifier = Modifier
                        .height(32.dp),
                    shape = RoundedCornerShape(slightRounding),
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                    colors = buttonColors(Color.DarkGray.copy(filterAlpha))
                ) {
                    Text(
                        text = stringResource(R.string.all),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(textAlpha),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Month.allValues.forEach { month ->
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(TextHandleMove)
                            onMonthSelected(month)
                        },
                        modifier = Modifier
                            .height(32.dp),
                        shape = RoundedCornerShape(slightRounding),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                        colors = buttonColors(Color.DarkGray.copy(filterAlpha))
                    ) {
                        Text(
                            text = month.getStringResource(context),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(textAlpha),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Rounded.Tune,
                contentDescription = stringResource(R.string.show_filters),
                modifier = Modifier
                    .padding(start = mediumPadding)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        role = Role.Button,
                        onClick = {
                            haptic.performHapticFeedback(TextHandleMove)
                            showFilters = !showFilters
                        }
                    ),
                tint = Color.White
            )
        }

        if (showFilters) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = regularPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(horizontalScrollState2)
                        .weight(1f)
                        .padding(horizontal = regularPadding),
                    horizontalArrangement = Arrangement.spacedBy(mediumPadding),
                ) {
                    FilterDropdown(
                        label = stringResource(R.string.type),
                        selectedValue = selectedEventType,
                        onValueSelected = onEventTypeSelected,
                        options = EventType.allValues.map { it.label },
                        stringToItem = { str -> EventType.entries.firstOrNull { it.label == str } },
                        itemToLabel = { eventType -> eventType?.label }
                    )

                    FilterDropdown(
                        label = stringResource(R.string.sound),
                        selectedValue = selectedSound,
                        onValueSelected = onSoundSelected,
                        options = uniqueSounds,
                        stringToItem = { it },
                        itemToLabel = { uniqueSounds -> uniqueSounds }
                    )

                    FilterDropdown(
                        label = stringResource(R.string.venue_),
                        selectedValue = selectedVenue,
                        onValueSelected = onVenueSelected,
                        options = uniqueLocations,
                        stringToItem = { it },
                        itemToLabel = { uniqueLocations -> uniqueLocations }
                    )
                }

                if (selectedEventType != null || selectedSound != null || selectedVenue != null) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.clear_filters),
                        modifier = Modifier
                            .padding(start = mediumPadding)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(),
                                role = Role.Button,
                                onClick = {
                                    haptic.performHapticFeedback(TextHandleMove)
                                    onEventTypeSelected(null)
                                    onSoundSelected(null)
                                    onVenueSelected(null)
                                }
                            ),
                        tint = Color.White
                    )
                }
            }
        }
    }
}