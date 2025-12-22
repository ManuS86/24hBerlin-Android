package com.esutor.twentyfourhoursberlin.ui.screens.components.utilitybars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.enums.EventType
import com.esutor.twentyfourhoursberlin.data.enums.Month
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.FilterDropdown
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
import com.esutor.twentyfourhoursberlin.ui.theme.roundRipple
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding
import kotlinx.coroutines.launch
import java.time.LocalDate.now

@Composable
fun FilterBar(eventVM: EventViewModel) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val uniqueLocations by eventVM.uniqueLocations.collectAsStateWithLifecycle()
    val uniqueSounds by eventVM.uniqueSounds.collectAsStateWithLifecycle()

    // --- State & Calculations ---
    val monthOptions = rememberSaveable(now()) {
        listOf<Month?>(null) + Month.dynamicOrder
    }
    val eventTypeOptions = remember(context) { EventType.entries }

    val selectedEventType by eventVM.selectedEventType.collectAsStateWithLifecycle()
    val selectedMonth by eventVM.selectedMonth.collectAsStateWithLifecycle()
    val selectedSound by eventVM.selectedSound.collectAsStateWithLifecycle()
    val selectedVenue by eventVM.selectedVenue.collectAsStateWithLifecycle()

    var showFilters by rememberSaveable { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()
    val horizontalScrollState2 = rememberScrollState()

    Column(
        modifier = Modifier.background(Black)
    ) {
        // --- Row 1: Months & Filter Button ---
        Row(
            modifier = Modifier
                .padding(bottom = halfPadding)
                .padding(end = regularPadding),
            verticalAlignment = CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .padding(start = regularPadding)
                    .weight(1f),
                horizontalArrangement = spacedBy(halfPadding)
            ) {
                monthOptions.forEach { month ->
                    val isSelected = selectedMonth == month

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(TextHandleMove)
                            eventVM.updateMonth(if (isSelected) null else month)
                        },
                        modifier = Modifier.height(32.dp),
                        shape = slightRounding,
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                        colors = buttonColors(DarkGray.copy(if (isSelected) 0.9f else 0.5f))
                    ) {
                        Text(
                            text = month?.getStringResource(context) ?: stringResource(R.string.all),
                            fontWeight = SemiBold,
                            color = White.copy(if (isSelected) 1f else 0.6f),
                            style = typography.bodyMedium
                        )
                    }
                }
            }

            // Show Dropdown Filters Button
            Icon(
                imageVector = Icons.Rounded.Tune,
                contentDescription = stringResource(R.string.show_filters),
                modifier = Modifier
                    .padding(start = halfPadding)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = roundRipple,
                        role = Role.Button,
                        onClick = {
                            haptic.performHapticFeedback(TextHandleMove)
                            showFilters = !showFilters
                        }
                    ),
                tint = White
            )
        }

        // --- Row 2: Secondary Filters (Dropdowns) ---
        AnimatedVisibility(
            visible = showFilters,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = regularPadding),
                verticalAlignment = CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(horizontalScrollState2)
                        .padding(start = regularPadding)
                        .weight(1f),
                    horizontalArrangement = spacedBy(halfPadding),
                ) {
                    FilterDropdown(
                        label = stringResource(R.string.type),
                        selectedValue = selectedEventType,
                        onValueSelected = { eventVM.updateEventType(it) },
                        options = eventTypeOptions,
                        itemToLabel = { type -> stringResource(type.labelRes) }
                    )

                    FilterDropdown(
                        label = stringResource(R.string.music),
                        selectedValue = selectedSound,
                        onValueSelected = { eventVM.updateSound(it) },
                        options = uniqueSounds,
                        itemToLabel = { sound -> sound }
                    )

                    FilterDropdown(
                        label = stringResource(R.string.venue),
                        selectedValue = selectedVenue,
                        onValueSelected = { eventVM.updateVenue(it) },
                        options = uniqueLocations,
                        itemToLabel = { venue -> venue }
                    )
                }

                // Clear Filters Button
                val hasActiveFilters = listOf(
                    selectedMonth,
                    selectedEventType,
                    selectedSound,
                    selectedVenue
                ).any { it != null }

                if (hasActiveFilters) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.clear_filters),
                        modifier = Modifier
                            .padding(start = halfPadding)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = roundRipple,
                                role = Role.Button,
                                onClick = {
                                    haptic.performHapticFeedback(TextHandleMove)
                                    eventVM.clearAllFilters()
                                    scope.launch {
                                        horizontalScrollState.animateScrollTo(0)
                                        horizontalScrollState2.animateScrollTo(0)
                                    }
                                }
                            ),
                        tint = White
                    )
                }
            }
        }
    }
}