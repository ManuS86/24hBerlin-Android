package com.esutor.twentyfourhoursberlin.ui.screens.components.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.Background
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding

@Composable
fun NoEventsState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(standardPadding),
        contentAlignment = Alignment.Center
    ) {
        Background()
        Text(
            text = stringResource(R.string.no_events_available),
            style = typography.headlineMedium,
            color = Gray.copy(0.6f),
            fontWeight = Bold,
            textAlign = Center
        )
    }
}