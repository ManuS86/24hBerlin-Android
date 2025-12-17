package com.example.a24hberlin.utils

import androidx.compose.ui.graphics.Color
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.theme.ArtAndCulture
import com.example.a24hberlin.ui.theme.Concert
import com.example.a24hberlin.ui.theme.Party

fun checkPassword(password: String, confirmPassword: String): Int? {
    if (password != confirmPassword) {
        return R.string.passwords_do_not_match
    }

    if (password.length < 8) {
        return R.string.password_must_be_at_least_8_characters_long
    }

    if (!password.matches(Regex(".*\\d.*"))) {
        return R.string.password_must_contain_at_least_one_number
    }

    if (!password.matches(Regex(".*[A-Z].*"))) {
        return R.string.password_must_contain_at_least_one_uppercase_letter
    }

    val specialCharacters = "!\"#$%&'()*+,-./:;<=>?@[]\\^_`{|}~"
    if (!password.any { it in specialCharacters }) {
        return R.string.password_must_contain_at_least_one_special_character
    }

    return null
}

fun Event.getEventColor(): Color {
    return when {
        this.eventType?.values?.contains("Konzert") == true -> Concert
        this.eventType?.values?.contains("Party") == true -> Party
        else -> ArtAndCulture
    }
}

fun String.toLanguageOrNull(): Language? {
    return Language.allValues.firstOrNull { it.label.equals(this, ignoreCase = true) }
}