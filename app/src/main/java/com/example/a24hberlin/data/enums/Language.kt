package com.example.a24hberlin.data.enums

import com.example.a24hberlin.R

enum class Language(val label: String, val resource: Int) {
    ENGLISH("english", R.string.english),
    GERMAN("german", R.string.german);

    companion object {
        val allValues = entries.toTypedArray()
    }
}