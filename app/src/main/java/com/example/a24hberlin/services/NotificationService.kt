package com.example.a24hberlin.services

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import coil3.Bitmap
import com.example.a24hberlin.R
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.receivers.ReminderNotificationReceiver
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class NotificationService(
    private val context: Context
) {
    companion object {
        lateinit var CHANNEL_ID: String
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        CHANNEL_ID = context.getString(R.string.event_reminder)
    }

    fun schedule14DayReminder() {
        val triggerDateTime = LocalDateTime.now().plusDays(14)
        val userTimeZone = ZoneId.systemDefault()
        val zonedTriggerDateTime = triggerDateTime.atZone(userTimeZone)
        val triggerMillis = zonedTriggerDateTime.toInstant().toEpochMilli()

        val notificationId = UUID.randomUUID().hashCode()

        val intent = createNotificationIntent(
            notificationId,
            context.getString(R.string.we_miss_you),
            context.getString(R.string.come_back_and_check_out_the_latest_events)
        )

        val pendingIntent = createPendingIntent(
            notificationId,
            intent
        )

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)

        println("14-day reminder scheduled for $triggerDateTime")
    }

    fun scheduleEventReminder(event: Event, dayModifier: Int, hourModifier: Int, image: Bitmap?) {
        val triggerDateTime = event.start
            .minusDays(dayModifier.toLong())
            .let {
                if (hourModifier > 0) it.minusHours(hourModifier.toLong()) else {
                    val eventTime = event.start.toLocalTime()
                    it.withHour(eventTime.hour).withMinute(eventTime.minute).withSecond(0)
                }
            }
        val userTimeZone = ZoneId.systemDefault()
        val zonedDateTime = triggerDateTime.atZone(userTimeZone)
        val triggerMillis = zonedDateTime.toInstant().toEpochMilli()

        val notificationId = event.id.hashCode()

        val body = when {
            dayModifier == 3 -> context.getString(R.string.dont_forget_event_3days, event.name)
            hourModifier == 3 -> context.getString(R.string.dont_forget_event_3hours, event.name)
            else -> context.getString(R.string.dont_forget_event_today, event.name)
        }

        val intent = createNotificationIntent(
            notificationId,
            context.getString(R.string.event_reminder),
            body,
            image
        )

        val pendingIntent = createPendingIntent(
            notificationId,
            intent
        )

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)

        println("Notification scheduled for ${event.name} at $triggerDateTime")
    }

    fun removeAllPendingNotifications() {
        notificationManager.cancelAll()
        println("All pending notifications removed")
    }

    fun unscheduleEventReminder(event: Event) {
        notificationManager.cancel(event.id.hashCode())
        println("Notification unscheduled for ${event.name}")
    }

    private fun createPendingIntent(
        notificationId: Int,
        intent: Intent
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
    }

    private fun createNotificationIntent(
        notificationId: Int,
        title: String,
        body: String,
        image: Bitmap? = null
    ): Intent {
        val intent = Intent(context, ReminderNotificationReceiver::class.java)
        intent.putExtra("channelId", CHANNEL_ID)
        intent.putExtra("notificationId", notificationId)
        intent.putExtra("title", title)
        intent.putExtra("body", body)

        if (image != null) {
            val stream = ByteArrayOutputStream()
            image.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            intent.putExtra("imageByteArray", byteArray)
        }
        return intent
    }
}