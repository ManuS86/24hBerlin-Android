package com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.utils.mediumRounding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapCard(event: Event) {
    val venue = LatLng(event.lat!!, event.long!!)
    val venueMarkerState = rememberMarkerState(position = venue)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(venue, 14f)
    }

    Card(
        Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(mediumRounding)
    ) {
        GoogleMap(
            Modifier.fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = venueMarkerState,
                title = event.name
            )
        }
    }
}