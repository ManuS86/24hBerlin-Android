package com.example.a24hberlin.data.model

import java.util.Date

data class AppUser(
    val id: String,
    var favoriteIDs: MutableList<String> = mutableListOf(),
    var registerDate: Date = Date(),
    var settings: Settings = Settings()
)
