package com.example.a24hberlin.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationManagerCompat
import coil3.Bitmap
import com.example.a24hberlin.MainActivity
import com.example.a24hberlin.R

class NotificationService(private val context: Context) {

    companion object {
        const val REMINDER_CHANNEL_ID = "EVENT_REMINDER_CHANNEL_ID"
    }

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(REMINDER_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun showNotification(
        title: String,
        body: String,
        image: Bitmap?,
        notificationId: Int
    ) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            activityIntent,
            FLAG_IMMUTABLE
        )

        val notification =
            NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(activityPendingIntent)
                .setAutoCancel(true)
                .setPriority(PRIORITY_HIGH)

        if (image != null) {
            notification.setLargeIcon(image)
        }

        notificationManager.notify(notificationId, notification.build())
    }
}