package com.example.a24hberlin.services

import com.example.a24hberlin.data.model.Event

interface ReminderScheduler {
    fun schedule14DayReminder()
    fun scheduleEventReminder(event: Event, dayModifier: Int, hourModifier: Int, imageURL: String?)
    fun cancelAllPendingReminders(favorites: List<Event>)
    fun cancelEventReminders(event: Event)
}