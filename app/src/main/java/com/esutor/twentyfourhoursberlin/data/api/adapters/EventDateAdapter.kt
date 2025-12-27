package com.esutor.twentyfourhoursberlin.data.api.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Custom Moshi Qualifiers.
 * These must be defined so Moshi knows how to link your data class fields
 * to the specific adapter functions.
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class StartDateTime

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class EndDateTime

@Suppress("unused")
class EventDateAdapter {

    // 1. Logic for the 'start' field (String -> LocalDateTime)
    @FromJson
    @StartDateTime
    fun startToDateTime(startStr: String): LocalDateTime {
        val seconds = startStr.toLongOrNull() ?: 0L
        return Instant.ofEpochSecond(seconds)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    @ToJson
    fun startToJson(@StartDateTime dateTime: LocalDateTime): String {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond().toString()
    }

    // 2. Logic for the 'end' field (Long? -> LocalDateTime?)
    @FromJson
    @EndDateTime
    fun endToDateTime(endLong: Long?): LocalDateTime? {
        return endLong?.let {
            Instant.ofEpochSecond(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }

    @ToJson
    fun endToJson(@EndDateTime dateTime: LocalDateTime?): Long? {
        return dateTime?.atZone(ZoneId.systemDefault())?.toEpochSecond()
    }
}