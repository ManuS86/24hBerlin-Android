package com.example.a24hberlin.data.repository

import com.example.a24hberlin.data.api.EventApi
import com.example.a24hberlin.data.model.Event

class EventRepository(
    private val apiService: EventApi
) {
    suspend fun loadEvents(): Map<String, Event> {
        val result = apiService.retrofitService.getEvents()
        return result.events
    }
}