package com.example.a24hberlin.data.repository

import com.example.a24hberlin.data.model.Event

interface EventRepository {
    suspend fun loadEvents(): Map<String, Event>
}