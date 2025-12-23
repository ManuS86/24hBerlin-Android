package com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.connectivitysnackbar

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.theme.Offline
import com.esutor.twentyfourhoursberlin.ui.theme.Online
import com.esutor.twentyfourhoursberlin.ui.theme.halfPadding
import com.esutor.twentyfourhoursberlin.ui.theme.mediumPadding
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding

@Composable
fun ConnectivitySnackbarHost(
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(hostState = snackbarHostState) { data ->
        val lostConnMessage = stringResource(R.string.no_internet_connection)
        val isOffline = data.visuals.message == lostConnMessage

        Snackbar(
            modifier = Modifier.padding(mediumPadding),
            action = {
                data.visuals.actionLabel?.let { label ->
                    TextButton(onClick = { data.performAction() }) {
                        Text(label, color = White)
                    }
                }
            },
            containerColor = if (isOffline) Offline else Online,
            contentColor = White,
            shape = slightRounding,
        ) {
            Row(
                verticalAlignment = CenterVertically,
                horizontalArrangement = spacedBy(halfPadding)
            ) {
                Icon(
                    imageVector = if (isOffline) Icons.Rounded.WifiOff else Icons.Rounded.Wifi,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(data.visuals.message)
            }
        }
    }
}