package com.example.a24hberlin.utils

import com.example.a24hberlin.data.enums.Language

fun checkPassword(password: String, confirmPassword: String): String {
    if (password != confirmPassword) {
        return "passwords_do_not_match"
    }

    if (password.length < 8) {
        return "password_must_be_at_least_8_characters_long"
    }

    if (!password.matches(Regex(".*\\d.*"))) {
        return "password_must_contain_at_least_one_number"
    }

    if (!password.matches(Regex(".*[A-Z].*"))) {
        return "password_must_contain_at_least_one_uppercase_letter"
    }

    val specialCharacters = "!\"#$%&'()*+,-./:;<=>?@[]\\^_`{|}~"
    if (!password.any { it in specialCharacters }) {
        return "password_must_contain_at_least_one_special_character"
    }

    return ""
}


fun String.toLanguageOrNull(): Language? {
    return Language.allValues.firstOrNull { it.label.equals(this, ignoreCase = true) }
}