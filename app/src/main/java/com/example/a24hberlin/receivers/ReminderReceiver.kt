package com.example.a24hberlin.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.a24hberlin.services.NotificationService

class ReminderReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val notificationService = NotificationService(context)
        val notificationId = intent.getIntExtra("notificationId", 0)
        val title = intent.getStringExtra("title") ?: ""
        val body = intent.getStringExtra("body") ?: ""
        val imageByteArray = intent.getByteArrayExtra("imageByteArray")

        notificationService.showNotification(title, body, imageByteArray, notificationId)
    }
}