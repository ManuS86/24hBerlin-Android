package com.example.a24hberlin.utils

import com.example.a24hberlin.data.enums.Language

fun String.toLanguageOrNull(): Language? {
    return Language.allValues.firstOrNull { it.label.equals(this, ignoreCase = true) }
}