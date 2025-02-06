package com.example.a24hberlin.data.enums

enum class Language(val label: String) {
    ENGLISH("english"),
    GERMAN("german");

    companion object {
        val allValues = entries.toTypedArray()
    }
}