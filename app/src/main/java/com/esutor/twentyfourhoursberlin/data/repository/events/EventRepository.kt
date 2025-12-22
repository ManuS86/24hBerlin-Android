package com.esutor.twentyfourhoursberlin.data.repository.events

import com.esutor.twentyfourhoursberlin.data.model.Event

interface EventRepository {
    suspend fun loadEvents(): Map<String, Event>
    suspend fun getEventsWithProcessedData(): List<Event>
}