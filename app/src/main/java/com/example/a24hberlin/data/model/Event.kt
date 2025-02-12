package com.example.a24hberlin.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@JsonClass(generateAdapter = true)
data class Event(
    var id: String = "",
    val content: String,
    val name: String,
    val permalink: String,
    @Json(name = "start") val startMillis: Long,
    @Json(name = "end") val endMillis: Long?,
    @Transient var startTime: LocalDateTime = LocalDateTime.now(),
    @Transient var endTime: LocalDateTime? = null,
    val details: String,
    val repeats: List<List<Long>>?,
    val subtitle: String?,
    @Json(name = "learnmore_link") val learnmoreLink: String?,
    val featured: String?,
    @Json(name = "image_url") val imageURL: String?,
    @Json(name = "location_name") val locationName: String?,
    @Json(name = "location_address") val address: String?,
    @Json(name = "location_lat") val lat: Double?,
    @Json(name = "location_lon") val long: Double?,
    @Json(name = "location_link") val locationLink: String?,
    @Json(name = "location_image") val locationImage: Map<String, String>?,
    @Json(name = "location_desc") val locationDesc: String?,
    @Json(name = "customfield_1") val entranceFee: EntranceFee?,
    @Json(name = "event_type") val eventType: Map<String, String>?,
    @Json(name = "event_type_2") val sounds: Map<String, String>?
) {
    init {
        startTime = Instant.ofEpochMilli(startMillis).atZone(ZoneId.of("UTC")).toLocalDateTime()
        endTime = endMillis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDateTime() }
    }
}