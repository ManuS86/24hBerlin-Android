package com.example.a24hberlin.ui.screens.clubmap

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.enums.Sound
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.filteredEvents
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun ClubMapScreen(
    searchText: TextFieldValue,
    selectedEventType: EventType?,
    selectedMonth: Month?,
    selectedSound: Sound?,
    selectedVenue: String?,
) {
    val berlinLatLng = LatLng(52.5200, 13.4050)
    val initialCameraPosition = CameraPosition.fromLatLngZoom(berlinLatLng, 11f)
    val cameraPositionState = rememberCameraPositionState {
        position = initialCameraPosition
    }
    val eventVM: EventViewModel = viewModel()
    val events by eventVM.events.collectAsState()
    val filteredEvents = filteredEvents(
        events = events,
        selectedMonth = selectedMonth,
        selectedEventType = selectedEventType,
        selectedSound = selectedSound,
        selectedVenue = selectedVenue,
        searchText = searchText
    )

    GoogleMap(
        Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        filteredEvents.forEach { event ->
            if (event.lat != null && event.long != null) {
                val venue = LatLng(event.lat, event.long)
                val venueMarkerState = rememberMarkerState(position = venue)
                Marker(
                    state = venueMarkerState,
                    title = event.name
                )
            }
        }
    }
}
