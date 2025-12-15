package com.example.a24hberlin.data.enums

import com.example.a24hberlin.R

enum class Language(val label: String, val resource: Int, val languageCode: String?) {

    ENGLISH("english", R.string.english, "en"),
    GERMAN("german", R.string.german, "de");

    companion object {
        val allValues = entries.toTypedArray()
    }
}