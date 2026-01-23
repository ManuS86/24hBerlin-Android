package com.esutor.twentyfourhoursberlin.notifications.reminderscheduler

import com.esutor.twentyfourhoursberlin.data.enums.EventReminderType
import com.esutor.twentyfourhoursberlin.data.models.Event

interface ReminderScheduler {
    fun scheduleAbsenceReminder()
    fun scheduleEventReminder(event: Event, type: EventReminderType, imageURL: String?)
    fun cancelAllPendingReminders(bookmarks: List<Event>)
    fun cancelEventReminders(event: Event)
}