package com.esutor.twentyfourhoursberlin.data.repository

import com.esutor.twentyfourhoursberlin.data.api.EventApi
import com.esutor.twentyfourhoursberlin.data.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class EventRepositoryImpl(private val apiService: EventApi) : EventRepository {

    override suspend fun loadEvents(): Map<String, Event> =
        withContext(Dispatchers.IO) {
            val result = apiService.retrofitService.getEvents()
            result.events
        }

    override suspend fun getEventsWithProcessedData(): List<Event> =
        withContext(Dispatchers.Default) {
            val eventsMap = loadEvents()

            eventsMap.map { (id, event) ->
                event.id = id
                event
            }.flatMap { event ->
                buildList {
                    add(event)

                    event.repeats?.forEachIndexed { index, repeatData ->
                        if (index == 0) return@forEachIndexed

                        val startTimeSeconds = repeatData.getOrNull(0)
                        val endTimeSeconds = repeatData.getOrNull(1)

                        add(
                            event.copy(
                                id = "${event.id}-${index}",
                                startSecs = startTimeSeconds?.toString() ?: event.startSecs,
                                endSecs = endTimeSeconds,
                                repeats = null
                            )
                        )
                    }
                }
            }.filter { event ->
                val now = LocalDateTime.now()
                val eventDateTime = event.end ?: event.start
                eventDateTime.isEqual(now) || eventDateTime.isAfter(now)
            }.sortedBy {
                it.start
            }
        }
}