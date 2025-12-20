package com.example.a24hberlin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.managers.NetworkMonitor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface ConnectionEvent {
    data object BackOnline : ConnectionEvent
    data object LostConnection : ConnectionEvent
}

class ConnectivityViewModel : ViewModel() {
    val isNetworkAvailable = NetworkMonitor.isConnected

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

            NetworkMonitor.isConnected.collect { isConnected ->
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