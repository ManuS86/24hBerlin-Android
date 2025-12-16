package com.example.a24hberlin.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.a24hberlin.data.repository.NetworkMonitor

class ConnectivityViewModel() : ViewModel() {
    val isNetworkAvailable = NetworkMonitor.isConnected
}