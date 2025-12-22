package com.esutor.twentyfourhoursberlin.managers.internetcopnnectionobserver

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}