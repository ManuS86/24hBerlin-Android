package com.example.a24hberlin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.managers.NetworkMonitor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

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
                    if (isConnected) {
                        _connectionEvent.send(ConnectionEvent.BackOnline)
                    } else {
                        _connectionEvent.send(ConnectionEvent.LostConnection)
                    }
                }
                lastState = isConnected
            }
        }
    }
}

sealed class ConnectionEvent {
    data object BackOnline : ConnectionEvent()
    data object LostConnection : ConnectionEvent()
}