package com.esutor.twentyfourhoursberlin.data.repositories.events

import com.esutor.twentyfourhoursberlin.data.models.Event

interface EventRepository {
    suspend fun getProcessedEvents(): List<Event>
    suspend fun loadEvents(): Map<String, Event>
    suspend fun transformToDisplayList(eventsMap: Map<String, Event>): List<Event>
}