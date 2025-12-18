package com.example.a24hberlin.workers

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.example.a24hberlin.notifications.NotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ImageNotificationWorker"

class ImageNotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val notificationService = NotificationService(applicationContext)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val title = inputData.getString("title")
        val body = inputData.getString("body")
        val notificationId = inputData.getInt("notificationId", 0)
        val imageURL = inputData.getString("imageURL")

        if (title == null || body == null) {
            return@withContext Result.failure()
        }

        var image: Bitmap? = null

        imageURL?.takeIf { it.isNotEmpty() }?.let { url ->
            try {
                val request = ImageRequest.Builder(applicationContext)
                    .data(url)
                    .allowHardware(false)
                    .build()

                val result = applicationContext.imageLoader.execute(request)

                if (result is SuccessResult) {
                    image = result.image.toBitmap()
                } else {
                    Log.w(TAG, "Image load was not successful: $result")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Coil image loading exception for URL: $url", e)
                e.printStackTrace()
            }
        }
        notificationService.showNotification(title, body, image, notificationId)
        return@withContext Result.success()
    }
}