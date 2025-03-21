package com.example.a24hberlin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a24hberlin.data.repository.PermissionRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PermissionViewModel(application: Application) : AndroidViewModel(application) {
    private val permissionRepo = PermissionRepositoryImpl(application)

    val hasNotificationPermission: StateFlow<Boolean> =
        permissionRepo.hasNotificationPermission.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            permissionRepo.hasNotificationPermission.value
        )

    fun updateNotificationPermission(isGranted: Boolean) {
        permissionRepo.updateNotificationPermission(isGranted)
    }
}