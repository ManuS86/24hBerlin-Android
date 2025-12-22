package com.esutor.twentyfourhoursberlin.data.repository.events

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

            eventsMap.flatMap { (originalId, event) ->
                if (event.repeats.isNullOrEmpty()) {
                    event.id = originalId
                    listOf(event)
                } else {
                    event.repeats.mapIndexed { index, repeatData ->
                        val startTimeSeconds = repeatData.getOrNull(0)
                        val endTimeSeconds = repeatData.getOrNull(1)

                        event.copy(
                            id = "$originalId-$index",
                            startSecs = startTimeSeconds?.toString() ?: event.startSecs,
                            endSecs = endTimeSeconds,
                            repeats = null
                        )
                    }
                }
            }.filter { event ->
                val now = LocalDateTime.now()
                val eventDateTime = event.end ?: event.start
                eventDateTime.isAfter(now) || eventDateTime.isEqual(now)
            }.sortedBy { it.start }
        }
}