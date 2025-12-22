package com.esutor.twentyfourhoursberlin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esutor.twentyfourhoursberlin.managers.internetcopnnectionobserver.AndroidConnectivityObserver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ConnectionEvent {
    data object BackOnline : ConnectionEvent
    data object LostConnection : ConnectionEvent
}

class ConnectivityViewModel(
    connectivityObserver: AndroidConnectivityObserver
) : ViewModel() {
    val isConnected = connectivityObserver
        .isConnected
        .stateIn(viewModelScope, WhileSubscribed(5000L), false)

    private val _connectionEvent = Channel<ConnectionEvent>(CONFLATED)
    val connectionEvent = _connectionEvent.receiveAsFlow()

    init {
        observeNetwork()
    }

    @Suppress("AssignedValueIsNeverRead")
    private fun observeNetwork() {
        viewModelScope.launch {
            // Initialize with null to ignore the very first emission from the StateFlow
            var lastState: Boolean? = null

            isConnected.collect { isConnected ->
                if (lastState != null && lastState != isConnected) {
                    val event =
                        if (isConnected) ConnectionEvent.BackOnline else ConnectionEvent.LostConnection
                    _connectionEvent.send(event)
                }
                lastState = isConnected
            }
        }
    }
}