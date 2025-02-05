package com.example.a24hberlin.data.model

import java.time.LocalDateTime

data class AppUser(
    val id: String,
    var favoriteIDs: MutableList<String> = mutableListOf(),
    var registerDate: LocalDateTime = LocalDateTime.now(),
    var settings: Settings = Settings()
)
