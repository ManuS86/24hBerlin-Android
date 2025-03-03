package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.a24hberlin.data.repository.PermissionRepositoryImpl
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel(application: Application) : AndroidViewModel(application) {
    private val permissionRepo = PermissionRepositoryImpl(application)

    val hasNotificationPermission: StateFlow<Boolean> =
        permissionRepo.hasNotificationPermission

    fun updateNotificationPermission(isGranted: Boolean) {
        permissionRepo.updateNotificationPermission(isGranted)
    }
}