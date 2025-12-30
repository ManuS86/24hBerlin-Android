package com.esutor.twentyfourhoursberlin.notifications.reminderscheduler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.enums.EventReminderType
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.notifications.ReminderReceiver
import com.esutor.twentyfourhoursberlin.notifications.ReminderReceiver.Companion.EXTRA_BODY
import com.esutor.twentyfourhoursberlin.notifications.ReminderReceiver.Companion.EXTRA_EVENT_ID
import com.esutor.twentyfourhoursberlin.notifications.ReminderReceiver.Companion.EXTRA_IMAGE_URL
import com.esutor.twentyfourhoursberlin.notifications.ReminderReceiver.Companion.EXTRA_NOTIFICATION_ID
import com.esutor.twentyfourhoursberlin.notifications.ReminderReceiver.Companion.EXTRA_TITLE
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidReminderScheduler(
    private val context: Context
) : ReminderScheduler {

    companion object {
        private const val TAG = "AlarmScheduling"
        const val ABSENCE_REMINDER_ID = 9999
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    private val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    /**
     * This check prevents the app from crashing or failing silently.
     */
    private fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33) and above: Force inexact to avoid permission hurdles
            false
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 (API 31/32): Check if permission was granted (it usually is by default here)
            alarmManager?.canScheduleExactAlarms() ?: false
        } else {
            // Below Android 12: No special permission required for exact alarms
            true
        }
    }

    @SuppressLint("MissingPermission")
    override fun schedule14DayReminder() {
        val triggerMillis = LocalDateTime.now()
            .plusDays(14)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val intent = createIntent(
            ABSENCE_REMINDER_ID,
            context.getString(R.string.we_miss_you),
            context.getString(R.string.come_back_and_check_out_the_latest_events)
        )

        scheduleAlarm(ABSENCE_REMINDER_ID, triggerMillis, intent)
    }

    @SuppressLint("MissingPermission")
    override fun scheduleEventReminder(
        event: Event,
        type: EventReminderType,
        imageURL: String?
    ) {
        val currentTime = System.currentTimeMillis()

        val triggerDateTime = when (type) {
            EventReminderType.THREE_DAYS_BEFORE -> event.start.minusDays(3)
            EventReminderType.TWELVE_HOURS_BEFORE -> event.start.minusHours(12)
            EventReminderType.THREE_HOURS_BEFORE -> event.start.minusHours(3)
        }

        val triggerMillis = triggerDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (triggerMillis <= currentTime) {
            Log.w(TAG, "Skipping past reminder: ${event.name} for $type")
            return
        }

        val alarmId = type.createAlarmId(event.id)

        val body = context.getString(
            when (type) {
                EventReminderType.THREE_DAYS_BEFORE -> R.string.dont_forget_event_3days
                EventReminderType.TWELVE_HOURS_BEFORE -> R.string.dont_forget_event_today
                EventReminderType.THREE_HOURS_BEFORE -> R.string.dont_forget_event_3hours
            },
            event.name
        )

        val intent = createIntent(
            notificationId = alarmId,
            title = context.getString(R.string.event_reminder),
            body = body,
            imageURL = imageURL,
            eventId = event.id
        )

        scheduleAlarm(alarmId, triggerMillis, intent)
        Log.d(TAG, "Scheduled: ${event.name} | Type: $type | ID: $alarmId at $triggerDateTime")
    }

    /**
     * Shared logic to handle exact vs inexact scheduling based on OS permissions.
     */
    @SuppressLint("MissingPermission")
    private fun scheduleAlarm(alarmId: Int, triggerMillis: Long, intent: Intent) {
        val pendingIntent = createPendingIntent(alarmId, intent)

        try {
            if (canScheduleExactAlarms()) {
                // Precise timing for older devices
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
            } else {
                // Inexact timing (Power efficient) for Android 13+ or restricted devices
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException: Exact alarm permission missing. Falling back to inexact.", e)
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
        }
    }

    override fun cancelAllPendingReminders(bookmarks: List<Event>) {
        cancelAlarm(ABSENCE_REMINDER_ID)
        bookmarks.forEach { cancelEventReminders(it) }
        Log.d(TAG, "All pending reminders removed")
    }

    override fun cancelEventReminders(event: Event) {
        EventReminderType.entries.forEach { type ->
            cancelAlarm(type.createAlarmId(event.id))
        }
        Log.d(TAG, "Reminders unscheduled for ${event.name}")
    }

    private fun cancelAlarm(alarmId: Int) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            pendingIntentFlags or PendingIntent.FLAG_NO_CREATE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun createPendingIntent(
        notificationId: Int,
        intent: Intent
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            pendingIntentFlags
        )
    }

    private fun createIntent(
        notificationId: Int,
        title: String,
        body: String,
        imageURL: String? = null,
        eventId: String? = null
    ): Intent {
        return Intent(context, ReminderReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_BODY, body)
            imageURL?.let { putExtra(EXTRA_IMAGE_URL, it) }
            eventId?.let { putExtra(EXTRA_EVENT_ID, it) }
        }
    }
}