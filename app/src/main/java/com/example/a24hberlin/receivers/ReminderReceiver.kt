package com.example.a24hberlin.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.a24hberlin.workers.ImageNotificationWorker

class ReminderReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 0)
        val title = intent.getStringExtra("title") ?: ""
        val body = intent.getStringExtra("body") ?: ""
        val imageURL = intent.getStringExtra("imageURL")

        val inputData = Data.Builder()
            .putString("title", title)
            .putString("body", body)
            .putString("imageURL", imageURL)
            .putInt("notificationId", notificationId)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ImageNotificationWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}