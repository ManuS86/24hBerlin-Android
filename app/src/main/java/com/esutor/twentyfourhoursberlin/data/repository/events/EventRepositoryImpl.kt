package com.esutor.twentyfourhoursberlin.data.repository.events

import com.esutor.twentyfourhoursberlin.data.api.EventApi
import com.esutor.twentyfourhoursberlin.data.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class EventRepositoryImpl(private val apiService: EventApi) : EventRepository {

    /**
     * Entry point: Orchestrates fetching, flattening, and filtering.
     */
    override suspend fun getProcessedEvents(): List<Event> {
        val rawData = loadEvents()
        return transformToDisplayList(rawData)
    }

    /**
     * Network Layer: Fetches raw data on IO thread.
     */
    override suspend fun loadEvents(): Map<String, Event> =
        withContext(Dispatchers.IO) {
            val result = apiService.retrofitService.getEvents()
            result.events
        }

    /**
     * Logic Layer: Processes data on Default (CPU) thread.
     */
    override suspend fun transformToDisplayList(eventsMap: Map<String, Event>): List<Event> =
        withContext(Dispatchers.Default) {
            val now = LocalDateTime.now()

            eventsMap
                .flatMap { (originalId, event) ->
                    expandRepeatableEvent(originalId, event)
                }
                .filter { event ->
                    isEventActive(event, now)
                }
                .sortedWith(
                    compareBy<Event> { it.start }
                        .thenBy { it.name }
                )
        }

    /**
     * Worker: Handles the conversion of the 'repeats' Long list into Event instances.
     */
    private fun expandRepeatableEvent(originalId: String, event: Event): List<Event> {
        return if (event.repeats.isNullOrEmpty()) {
            // Since Event is now immutable, we use .copy() to set the ID
            listOf(event.copy(id = originalId))
        } else {
            event.repeats.mapIndexed { index, repeatData ->
                val startTimeSeconds = repeatData.getOrNull(0)
                val endTimeSeconds = repeatData.getOrNull(1)

                // Manually convert the Longs from the repeat list to LocalDateTime
                val newStart = startTimeSeconds?.let {
                    Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
                } ?: event.start

                val newEnd = endTimeSeconds?.let {
                    Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
                }

                event.copy(
                    id = "$originalId-$index",
                    start = newStart,
                    end = newEnd,
                    repeats = null
                )
            }
        }
    }

    /**
     * Worker: Determines if an event should still be shown.
     */
    private fun isEventActive(event: Event, now: LocalDateTime): Boolean {
        // If no end time exists, it expires at midnight the following day
        val expiryTime = event.end ?: event.start.plusDays(1).toLocalDate().atStartOfDay()
        return expiryTime.isAfter(now) || expiryTime.isEqual(now)
    }
}