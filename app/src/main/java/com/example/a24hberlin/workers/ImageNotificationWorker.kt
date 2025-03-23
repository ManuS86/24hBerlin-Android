package com.example.a24hberlin.workers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.a24hberlin.services.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class ImageNotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val title = inputData.getString("title") ?: return Result.failure()
        val body = inputData.getString("body") ?: return Result.failure()
        val imageURL = inputData.getString("imageURL")
        val notificationId = inputData.getInt("notificationId", 0)

        val notificationService = NotificationService(applicationContext)

        if (imageURL != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val url = URL(imageURL)
                    val input = url.openStream()
                    val image = BitmapFactory.decodeStream(input)

                    withContext(Dispatchers.Main) {
                        notificationService.showNotification(title, body, image, notificationId)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        notificationService.showNotification(title, body, null, notificationId)
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                notificationService.showNotification(title, body, null, notificationId)
            }
        }
        return Result.success()
    }
}