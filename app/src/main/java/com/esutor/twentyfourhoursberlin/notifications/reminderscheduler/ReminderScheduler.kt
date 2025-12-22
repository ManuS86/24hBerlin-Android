package com.esutor.twentyfourhoursberlin.notifications.reminderscheduler

import com.esutor.twentyfourhoursberlin.data.enums.EventReminderType
import com.esutor.twentyfourhoursberlin.data.model.Event

interface ReminderScheduler {
    fun schedule14DayReminder()
    fun scheduleEventReminder(event: Event, type: EventReminderType, imageURL: String?)
    fun cancelAllPendingReminders(bookmarks: List<Event>)
    fun cancelEventReminders(event: Event)
}