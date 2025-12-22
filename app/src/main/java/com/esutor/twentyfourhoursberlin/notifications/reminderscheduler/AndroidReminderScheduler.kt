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
import java.time.LocalDateTime
import java.time.ZoneId

class AndroidReminderScheduler(
    private val context: Context
) : ReminderScheduler {

    companion object {
        const val ABSENCE_REMINDER_ID = 9999
    }

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    private val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    @SuppressLint("MissingPermission")
    override fun schedule14DayReminder() {
        val triggerDateTime = LocalDateTime.now().plusDays(14)
        val userTimeZone = ZoneId.systemDefault()
        val zonedTriggerDateTime = triggerDateTime.atZone(userTimeZone)
        val triggerMillis = zonedTriggerDateTime.toInstant().toEpochMilli()

        val notificationId = ABSENCE_REMINDER_ID

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
        type: EventReminderType,
        imageURL: String?
    ) {
        val currentTime = System.currentTimeMillis()

        val triggerDateTime = when (type) {
            EventReminderType.THREE_DAYS_BEFORE -> event.start.minusDays(3)
            EventReminderType.TWELVE_HOURS_BEFORE -> event.start.minusHours(12)
            EventReminderType.THREE_HOURS_BEFORE -> event.start.minusHours(3)
        }

        val userTimeZone = ZoneId.systemDefault()
        val zonedDateTime = triggerDateTime.atZone(userTimeZone)
        val triggerMillis = zonedDateTime.toInstant().toEpochMilli()

        val alarmId = type.createAlarmId(event.id)

        val body = when (type) {
            EventReminderType.THREE_DAYS_BEFORE -> context.getString(
                R.string.dont_forget_event_3days,
                event.name
            )
            EventReminderType.TWELVE_HOURS_BEFORE -> context.getString(
                R.string.dont_forget_event_today,
                event.name
            )
            EventReminderType.THREE_HOURS_BEFORE -> context.getString(
                R.string.dont_forget_event_3hours,
                event.name
            )
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


    override fun cancelAllPendingReminders(bookmarks: List<Event>) {
        alarmManager.cancel(
            createPendingIntent(
                ABSENCE_REMINDER_ID,
                Intent(context, ReminderReceiver::class.java)
            )
        )

        bookmarks.forEach { bookmark ->
            cancelEventReminders(bookmark)
        }

        Log.d(
            "AlarmCancelling",
            "All pending reminders removed"
        )
    }

    override fun cancelEventReminders(event: Event) {
        EventReminderType.entries.forEach { type ->
            val alarmId = type.createAlarmId(event.id)

            alarmManager.cancel(
                createPendingIntent(
                    alarmId,
                    Intent(context, ReminderReceiver::class.java)
                )
            )
        }

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
            pendingIntentFlags
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

        if (imageURL != null) { intent.putExtra("imageURL", imageURL) }
        return intent
    }
}