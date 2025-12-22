package com.esutor.twentyfourhoursberlin.data.enums

import com.esutor.twentyfourhoursberlin.R

enum class Language(val label: String, val resource: Int, val languageCode: String?) {

    ENGLISH("english", R.string.english, "en"),
    GERMAN("german", R.string.german, "de");
}