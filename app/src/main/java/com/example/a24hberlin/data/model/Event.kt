package com.example.a24hberlin.data.model

import java.time.LocalDateTime

data class Event(
    var id: String,
    val content: String,
    val name: String,
    val permalink: String,
    var start: LocalDateTime,
    var end: LocalDateTime?,
    val details: String,
    val repeats: List<Pair<LocalDateTime, LocalDateTime>>?,
    val subtitle: String?,
    val learnMoreLink : String?,
    val featured : String?,
    val imageURL : String?,
    val locationName : String?,
    val address : String?,
    val lat : Double?,
    val long : Double?,
    val locationLink : String?,
    val locationImage: Map<String, String>?,
    val locationDesc: String?,
    val entranceFee: EntranceFee?,
    val eventType: Map<String, String>?,
    val sounds: Map<String, String>?,
)