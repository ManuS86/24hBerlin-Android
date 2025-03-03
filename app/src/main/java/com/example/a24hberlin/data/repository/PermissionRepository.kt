package com.example.a24hberlin.data.repository

import kotlinx.coroutines.flow.StateFlow

interface PermissionRepository {
    val hasNotificationPermission: StateFlow<Boolean>
    fun updateNotificationPermission(isGranted: Boolean)
}