package com.example.a24hberlin.ui.screens.clubmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.screens.components.event.item.EventItem
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.ui.theme.halfPadding
import com.example.a24hberlin.ui.theme.regularPadding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubMapScreen(eventVM: EventViewModel) {
    val haptic = LocalHapticFeedback.current
    val sheetState = rememberModalBottomSheetState()

    val events by eventVM.filteredEvents.collectAsStateWithLifecycle()

    var selectedEvent: Event? by rememberSaveable { mutableStateOf(null) }
    var showEventSheet by rememberSaveable { mutableStateOf(false) }

    val berlinLatLng = LatLng(52.5200, 13.4050)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(berlinLatLng, 11f)
    }

    LaunchedEffect(Unit) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(berlinLatLng, 11f)
    }

    DisposableEffect(Unit) {
        onDispose {
            showEventSheet = false
            selectedEvent = null
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        events.forEach { event ->
            val eventLatLng = remember(event.id, event.lat, event.long) {
                if (event.lat != null && event.long != null) {
                    LatLng(event.lat, event.long)
                } else null
            }

            eventLatLng?.let { latLng ->
                Marker(
                    state = rememberMarkerState(key = event.id, position = latLng),
                    title = event.name,
                    onClick = {
                        haptic.performHapticFeedback(TextHandleMove)
                        selectedEvent = event
                        showEventSheet = true
                        true
                    }
                )
            }
        }
    }

    if (showEventSheet) {
        @Suppress("AssignedValueIsNeverRead")
        ModalBottomSheet(
            onDismissRequest = { showEventSheet = false },
            containerColor = White,
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .padding(horizontal = regularPadding)
                    .padding(bottom = halfPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                selectedEvent?.let {
                    EventItem(
                        event = it,
                        isExpandable = false,
                        isInitiallyExpanded = true
                    )
                }
            }
        }
    }
}
