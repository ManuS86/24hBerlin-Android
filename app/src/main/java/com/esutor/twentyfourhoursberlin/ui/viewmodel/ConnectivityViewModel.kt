package com.esutor.twentyfourhoursberlin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esutor.twentyfourhoursberlin.managers.internetconnectionobserver.AndroidConnectivityObserver
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn

class ConnectivityViewModel(
    connectivityObserver: AndroidConnectivityObserver
) : ViewModel() {
    val isConnected = connectivityObserver
        .isConnected
        .stateIn(viewModelScope, WhileSubscribed(5000L), false)
}