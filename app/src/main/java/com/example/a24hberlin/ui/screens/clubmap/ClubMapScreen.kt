package com.example.a24hberlin.ui.screens.clubmap

import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.screens.components.eventitem.EventItem
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.filteredEvents
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding
import com.example.a24hberlin.utils.smallPadding
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
    val view = LocalView.current
    val eventVM: EventViewModel = viewModel()

    val berlinLatLng = LatLng(52.5200, 13.4050)

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

    val initialCameraPosition = CameraPosition.fromLatLngZoom(berlinLatLng, 11f)
    val cameraPositionState = rememberCameraPositionState {
        position = initialCameraPosition
    }
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()

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
                        view.playSoundEffect(SoundEffectConstants.CLICK)
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
            containerColor = Color.White,
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .padding(horizontal = regularPadding)
                    .padding(bottom = mediumPadding)
                    .verticalScroll(scrollState)
            ) {
                selectedEvent?.let {
                    EventItem(it, isExpandable = false, isInitiallyExpanded = true)
                }
            }
        }
    }
}
