package com.example.a24hberlin.notifications.schedule

import com.example.a24hberlin.data.enums.EventReminderType
import com.example.a24hberlin.data.model.Event

interface ReminderScheduler {
    fun schedule14DayReminder()
    fun scheduleEventReminder(event: Event, type: EventReminderType, imageURL: String?)
    fun cancelAllPendingReminders(bookmarks: List<Event>)
    fun cancelEventReminders(event: Event)
}