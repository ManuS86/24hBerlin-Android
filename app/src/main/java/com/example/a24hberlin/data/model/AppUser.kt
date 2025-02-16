package com.example.a24hberlin.data.model

import com.google.firebase.Timestamp
import java.util.Date

data class AppUser(
    var favoriteIDs: MutableList<String> = mutableListOf(),
    var registerDate: Timestamp = Timestamp(Date()),
    var settings: Settings = Settings()
)
