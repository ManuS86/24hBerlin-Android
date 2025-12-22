package com.esutor.twentyfourhoursberlin.managers

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class LanguageChangeHelper {
    fun setLanguage(context: Context, languageCode: String) {
        val locales = if (languageCode.isEmpty()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(languageCode)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeList = if (languageCode.isEmpty()) LocaleList.getEmptyLocaleList()
            else LocaleList.forLanguageTags(languageCode)
            context.getSystemService(LocaleManager::class.java).applicationLocales = localeList
        } else {
            AppCompatDelegate.setApplicationLocales(locales)
        }

        if (context is Activity) {
            context.recreate()
        }
    }
}