package com.example.a24hberlin.data.enums

import android.content.Context
import com.example.a24hberlin.R
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

    private val englishName: String
        get() = java.time.Month.of(value).getDisplayName(TextStyle.FULL, Locale.US).lowercase()

    companion object {
        val allValues = entries.toTypedArray()

        private val monthResourceMap = mapOf(
            "january" to R.string.january,
            "february" to R.string.february,
            "march" to R.string.march,
            "april" to R.string.april,
            "may" to R.string.may,
            "june" to R.string.june,
            "july" to R.string.july,
            "august" to R.string.august,
            "september" to R.string.september,
            "october" to R.string.october,
            "november" to R.string.november,
            "december" to R.string.december
        )
    }

    fun getStringResource(context: Context): String {
        val resourceId = monthResourceMap[englishName]

        return if (resourceId != null) {
            context.getString(resourceId)
        } else {
            englishName
        }
    }
}
