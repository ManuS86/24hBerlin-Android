package com.example.a24hberlin.data.enums

enum class EventType(val label: String) {
    CONCERT("Konzert"),
    ART_AND_CULTURE("Kunst & Kultur"),
    PARTY("Party");

    companion object {
        val allValues = entries.toTypedArray()
    }
}