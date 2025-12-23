package com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables.connectivitysnackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.ui.viewmodel.ConnectivityViewModel

@Composable
fun ConnectivitySnackbarEffect(
    viewModel: ConnectivityViewModel,
    hostState: SnackbarHostState
) {
    val isOnline by viewModel.isConnected.collectAsStateWithLifecycle()
    val noInternetMsg = stringResource(R.string.no_internet_connection)
    val backOnlineMsg = stringResource(R.string.back_online)

    val wasPreviouslyOffline = remember { mutableStateOf(false) }
    val isInitialComposition = remember { mutableStateOf(true) }

    LaunchedEffect(isOnline) {
        if (isOnline) {
            hostState.currentSnackbarData?.let {
                if (it.visuals.message == noInternetMsg) it.dismiss()
            }

            if (!isInitialComposition.value && wasPreviouslyOffline.value) {
                hostState.showSnackbar(backOnlineMsg, duration = SnackbarDuration.Short)
                wasPreviouslyOffline.value = false
            }
        } else {
            wasPreviouslyOffline.value = true
            hostState.showSnackbar(
                message = noInternetMsg,
                duration = SnackbarDuration.Indefinite,
                withDismissAction = true
            )
        }

        isInitialComposition.value = false
    }
}