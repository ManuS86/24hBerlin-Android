package com.example.a24hberlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(getString(R.string.event_reminder), name, importance).apply {
                description = descriptionText
            }
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.createNotificationChannel(channel)
    }
}