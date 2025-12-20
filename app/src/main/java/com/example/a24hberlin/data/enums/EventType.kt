package com.example.a24hberlin.data.enums

import androidx.annotation.StringRes
import com.example.a24hberlin.R

enum class EventType(@get:StringRes val labelRes: Int) {
    CONCERT(R.string.concert),
    ART_AND_CULTURE(R.string.art_and_culture),
    PARTY(R.string.party);

    companion object {
        fun fromString(type: String?): EventType? {
            return when (type?.lowercase()) {
                "konzert" -> CONCERT
                "kunst & kultur" -> ART_AND_CULTURE
                "party" -> PARTY
                else -> null
            }
        }
    }
}