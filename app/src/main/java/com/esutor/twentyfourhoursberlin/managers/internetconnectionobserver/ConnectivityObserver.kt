package com.esutor.twentyfourhoursberlin.managers.internetconnectionobserver

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}