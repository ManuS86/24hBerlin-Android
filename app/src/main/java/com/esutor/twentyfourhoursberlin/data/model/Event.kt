package com.esutor.twentyfourhoursberlin.data.model

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
    @Json(name = "start") var startSecs: String,
    @Json(name = "end") var endSecs: Long?,
    @Transient var start: LocalDateTime = LocalDateTime.now(),
    @Transient var end: LocalDateTime? = null,
    val details: String,
    val repeats: List<List<Long>>?,
    @Json(name = "event_subtitle") val subtitle: String?,
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
        val startMillisLong = startSecs.toLong()

        start = Instant.ofEpochSecond(startMillisLong).atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        end = endSecs?.let {
            Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }
}