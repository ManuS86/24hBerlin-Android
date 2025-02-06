package com.example.a24hberlin.data.enums

import java.time.format.TextStyle
import java.util.Locale

enum class Month(val value: Int) {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    val englishName: String
        get() = java.time.Month.of(value).getDisplayName(TextStyle.FULL, Locale.US).lowercase()

    companion object {
        val allValues = entries.toTypedArray()

        fun fromInt(value: Int): Month? = allValues.firstOrNull { it.value == value }
    }
}
