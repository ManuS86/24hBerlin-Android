package com.esutor.twentyfourhoursberlin.managers.permissionmanager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface PermissionManager {
    val _hasNotificationPermission: MutableStateFlow<Boolean>
    val hasNotificationPermission: StateFlow<Boolean>
    fun updateNotificationPermission(isGranted: Boolean)
}