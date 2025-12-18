package com.example.a24hberlin.ui.screens.clubmap

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.filteredEvents
import com.example.a24hberlin.ui.theme.mediumPadding
import com.example.a24hberlin.ui.theme.regularPadding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubMapScreen(
    searchText: TextFieldValue,
    selectedEventType: EventType?,
    selectedMonth: Month?,
    selectedSound: String?,
    selectedVenue: String?
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()

    val eventVM: EventViewModel = viewModel(viewModelStoreOwner = context as ComponentActivity)
    val events by eventVM.events.collectAsStateWithLifecycle()

    var selectedEvent: Event? by remember { mutableStateOf(null) }
    var showEventSheet by remember { mutableStateOf(false) }

    val filteredEvents = filteredEvents(
        events = events,
        selectedMonth = selectedMonth,
        selectedEventType = selectedEventType,
        selectedSound = selectedSound,
        selectedVenue = selectedVenue,
        searchText = searchText
    )

    val berlinLatLng = LatLng(52.5200, 13.4050)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(berlinLatLng, 11f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        filteredEvents.forEach { event ->
            if (event.lat != null && event.long != null) {
                val venuePosition = LatLng(event.lat, event.long)
                Marker(
                    state = rememberMarkerState(position = venuePosition),
                    title = event.name,
                    onClick = {
                        haptic.performHapticFeedback(TextHandleMove)
                        selectedEvent = event
                        showEventSheet = !showEventSheet
                        true
                    }
                )
            }
        }
    }

    if (showEventSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEventSheet = false },
            containerColor = White,
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .padding(horizontal = regularPadding)
                    .padding(bottom = mediumPadding)
                    .verticalScroll(scrollState)
            ) {
                selectedEvent?.let {
                    EventItem(
                        event = it,
                        eventVM = eventVM,
                        isExpandable = false,
                        isInitiallyExpanded = true
                    )
                }
            }
        }
    }
}
