package com.example.a24hberlin.managers

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidPermissionManager(application: Application) {
    private val _hasNotificationPermission = MutableStateFlow(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    )

    val hasNotificationPermission =
        _hasNotificationPermission.asStateFlow()

    fun updateNotificationPermission(isGranted: Boolean) {
        _hasNotificationPermission.value = isGranted
    }
}