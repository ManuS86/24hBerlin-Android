package com.example.a24hberlin.utils.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.a24hberlin.MainActivity
import com.example.a24hberlin.R
import com.example.a24hberlin.data.model.Event
import java.util.Calendar
import java.util.Date
import java.util.UUID

class NotificationService(
    private val context: Context
) {

    private val activityIntent = Intent(context, MainActivity::class.java)
    private val activityPendingIntent = PendingIntent.getActivity(
        context,
        1,
        activityIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    companion object {
        private const val NOTIFICATION_KEY = "LastAppOpenDate"
        lateinit var CHANNEL_ID: String
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        CHANNEL_ID = context.getString(R.string.event_reminder)
    }

    fun schedule14DayReminder() {
        val triggerDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 14)
        }.time

        val notificationId = "14DayReminder-${UUID.randomUUID()}"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.we_miss_you))
            .setContentText(context.getString(R.string.come_back_and_check_out_the_latest_events))
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationId.hashCode(), notification)

        context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
            .putLong(NOTIFICATION_KEY, Date().time).apply()

        println("14-day reminder scheduled for $triggerDate")
    }

    fun scheduleEventReminder(event: Event, dayModifier: Int, hourModifier: Int) {
        val triggerDateTime = event.start
            .minusDays(dayModifier.toLong())
            .let {
                if (hourModifier > 0) it.minusHours(hourModifier.toLong()) else {
                    val eventTime = event.start.toLocalTime()
                    it.withHour(eventTime.hour).withMinute(eventTime.minute).withSecond(0)
                }
            }

        val notificationId = event.id.hashCode()

        val body = when {
            dayModifier == 3 -> context.getString(R.string.dont_forget_event_3days, event.name)
            hourModifier == 2 -> context.getString(R.string.dont_forget_event_3hours, event.name)
            else -> context.getString(R.string.dont_forget_event_today, event.name)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.event_reminder))
            .setContentText(body)
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(notificationId, notification)

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

    fun updateLastAppOpenDate() {
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit()
            .putLong(NOTIFICATION_KEY, Date().time).apply()
    }
}