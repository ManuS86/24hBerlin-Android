package com.example.a24hberlin.services

import coil3.Bitmap
import com.example.a24hberlin.data.model.Event

interface ReminderScheduler {
    fun schedule14DayReminder()
    fun scheduleEventReminder(event: Event, dayModifier: Int, hourModifier: Int, image: Bitmap?)
    fun cancelAllPendingReminders(favorites: List<Event>)
    fun cancelEventReminder(event: Event)
}