package com.esutor.twentyfourhoursberlin.data.enums

import android.content.Context
import com.esutor.twentyfourhoursberlin.R
import java.time.LocalDate.now
import java.time.format.TextStyle
import java.util.Locale.US

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
        get() = java.time.Month.of(value)
            .getDisplayName(TextStyle.FULL, US)
            .lowercase()

    fun getStringResource(context: Context): String {
        val resourceId = monthResourceMap[englishName]
        return if (resourceId != null) context.getString(resourceId) else englishName
    }

    companion object {
        /**
         * Returns months starting from the current month to the end of the 12-month cycle.
         * Example: If today is October, returns [OCT, NOV, DEC, JAN, ... SEP]
         */
        val dynamicOrder: List<Month>
            get() {
                val currentMonthValue = now().monthValue // 1-12
                val entries = entries
                val startIndex = currentMonthValue - 1

                return entries.drop(startIndex) + entries.take(startIndex)
            }

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
}
