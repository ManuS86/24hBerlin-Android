package com.esutor.twentyfourhoursberlin.managers.permissionmanager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidPermissionManager(
    private val context: Context
) : PermissionManager {
    override val _hasNotificationPermission = MutableStateFlow(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    )

    override val hasNotificationPermission =
        _hasNotificationPermission.asStateFlow()

    override fun updateNotificationPermission(isGranted: Boolean) {
        _hasNotificationPermission.value = isGranted
    }
}