package com.example.a24hberlin.services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.a24hberlin.R
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.receivers.ReminderReceiver
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidReminderScheduler(
    private val context: Context
) : ReminderScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    override fun schedule14DayReminder() {
        val triggerDateTime = LocalDateTime.now().plusDays(14)
        val userTimeZone = ZoneId.systemDefault()
        val zonedTriggerDateTime = triggerDateTime.atZone(userTimeZone)
        val triggerMillis = zonedTriggerDateTime.toInstant().toEpochMilli()

        val notificationId = 1.hashCode()

        val intent = createIntent(
            notificationId,
            context.getString(R.string.we_miss_you),
            context.getString(R.string.come_back_and_check_out_the_latest_events)
        )

        val pendingIntent = createPendingIntent(
            notificationId,
            intent
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            pendingIntent
        )

        Log.d(
            "AlarmScheduling",
            "14-day reminder scheduled for $triggerDateTime"
        )
    }

    @SuppressLint("MissingPermission")
    override fun scheduleEventReminder(
        event: Event,
        dayModifier: Int,
        hourModifier: Int,
        imageURL: String?
    ) {
        val currentTime = System.currentTimeMillis()
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

        val alarmId = (event.id.hashCode() * 10) + dayModifier + hourModifier

        val body = when {
            dayModifier == 3 -> context.getString(R.string.dont_forget_event_3days, event.name)
            hourModifier == 3 -> context.getString(R.string.dont_forget_event_3hours, event.name)
            else -> context.getString(R.string.dont_forget_event_today, event.name)
        }

        if (triggerMillis > currentTime) {
            val intent = createIntent(
                alarmId,
                context.getString(R.string.event_reminder),
                body,
                imageURL
            )

            val pendingIntent = createPendingIntent(
                alarmId,
                intent
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )

            Log.d(
                "AlarmScheduling",
                "Notification scheduled for ${event.name} at $triggerDateTime"
            )
        } else {
            Log.w(
                "AlarmScheduling",
                "Alarm time is in the past, skipping alarm for event: ${event.name}, alarmId: $alarmId"
            )
        }
    }


    override fun cancelAllPendingReminders(favorites: List<Event>) {
        alarmManager.cancel(
            createPendingIntent(
                1.hashCode(),
                Intent(context, ReminderReceiver::class.java)
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll()
        } else {
            favorites.forEach { favorite ->
                alarmManager.cancel(
                    createPendingIntent(
                        favorite.id.hashCode(),
                        Intent(context, ReminderReceiver::class.java)
                    )
                )
            }
        }
        Log.d(
            "AlarmCancelling",
            "All pending reminders removed"
        )
    }

    override fun cancelEventReminders(event: Event) {
        alarmManager.cancel(
            createPendingIntent(
                (event.id.hashCode() * 10) + 3 + 12,
                Intent(context, ReminderReceiver::class.java)
            )
        )
        alarmManager.cancel(
            createPendingIntent(
                (event.id.hashCode() * 10) + 0 + 12,
                Intent(context, ReminderReceiver::class.java)
            )
        )
        alarmManager.cancel(
            createPendingIntent(
                (event.id.hashCode() * 10) + 0 + 3,
                Intent(context, ReminderReceiver::class.java)
            )
        )
        Log.d(
            "AlarmCancelling",
            "Reminders unscheduled for ${event.name}"
        )
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
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
    }

    private fun createIntent(
        notificationId: Int,
        title: String,
        body: String,
        imageURL: String? = null
    ): Intent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("notificationId", notificationId)
            putExtra("title", title)
            putExtra("body", body)

        }

        if (imageURL != null) {
            intent.putExtra("imageURL", imageURL)
        }
        return intent
    }
}