package com.example.a24hberlin.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.a24hberlin.MainActivity
import com.example.a24hberlin.R

class NotificationService(
    private val context: Context
) {

    private val notificationManager = NotificationManagerCompat.from(context)

    @SuppressLint("MissingPermission")
    fun showNotification(
        title: String,
        body: String,
        imageByteArray: ByteArray?,
        notificationId: Int
    ) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification =
            NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(activityPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (imageByteArray != null) {
            val image = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            notification.setLargeIcon(image)
        }

        notificationManager.notify(notificationId, notification.build())
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "Event Reminder"
    }
}