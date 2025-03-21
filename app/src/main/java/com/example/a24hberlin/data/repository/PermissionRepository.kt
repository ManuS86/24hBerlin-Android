package com.example.a24hberlin.data.repository

interface PermissionRepository {
    fun updateNotificationPermission(isGranted: Boolean)
}