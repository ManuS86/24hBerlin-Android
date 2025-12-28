package com.esutor.twentyfourhoursberlin.notifications

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_NOTIFICATION_ID = "notificationId"
        const val EXTRA_TITLE = "title"
        const val EXTRA_BODY = "body"
        const val EXTRA_IMAGE_URL = "imageURL"
        const val EXTRA_EVENT_ID = "eventId"
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE)
        val body = intent.getStringExtra(EXTRA_BODY)

        if (title == null || body == null) return

        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        val imageURL = intent.getStringExtra(EXTRA_IMAGE_URL)
        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)

        val inputData = Data.Builder()
            .putString("title", title)
            .putString("body", body)
            .putString("imageURL", imageURL)
            .putString("eventId", eventId)
            .putInt("notificationId", notificationId)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ImageNotificationWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}