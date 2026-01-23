package com.esutor.twentyfourhoursberlin.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.enums.Language
import com.esutor.twentyfourhoursberlin.data.models.Event
import com.esutor.twentyfourhoursberlin.ui.theme.ArtAndCulture
import com.esutor.twentyfourhoursberlin.ui.theme.Concert
import com.esutor.twentyfourhoursberlin.ui.theme.Party

// Events
private fun Event.matches(category: String): Boolean {
    return this.eventType?.values?.any {
        it.replace("&amp;", "&").equals(category, ignoreCase = true)
    } ?: false
}

fun Event.getEventColor(): Color = when {
    matches("Konzert") -> Concert
    matches("Party") -> Party
    matches("Kunst & Kultur") -> ArtAndCulture
    else -> Party
}

@DrawableRes
fun Event.getMarkerResourceId(): Int = when {
    matches("Konzert") -> R.drawable.ic_concert_location
    matches("Party") -> R.drawable.ic_party_location
    matches("Kunst & Kultur") -> R.drawable.ic_art_and_culture_location
    else -> R.drawable.ic_party_location
}

// Strings
fun String.cleanToAnnotatedString(): AnnotatedString {
    val cleanText = this.extractCleanContent()
    return cleanText.toAnnotatedString()
}

/**
 * Handles the logic of stripping JS boilerplate and extracting inner content.
 */
fun String.extractCleanContent(): String {
    if (this.isBlank()) return ""

    // Decodes HTML entities (like &amp;) first to make Regex more reliable
    val decoded = HtmlCompat.fromHtml(this, FROM_HTML_MODE_LEGACY).toString()

    // Extracts content between backticks
    val jsContentRegex = Regex("`([^`]+)`")
    val extracted = jsContentRegex.find(decoded)?.groupValues?.get(1) ?: ""

    // Strips out the document.getElementById boilerplate
    val boilerplateRegex = Regex(
        """document\.getElementById\(.*?\)\.innerHTML\s*=\s*[`'"].*?[`'"]\s*;""",
        RegexOption.DOT_MATCHES_ALL
    )
    val noJS = decoded.replace(boilerplateRegex, "").trim()

    return if (extracted.isNotBlank()) "$noJS\n\n$extracted" else noJS
}

/**
 * Converts a string (potentially with HTML tags) into a Compose AnnotatedString.
 */
fun String.toAnnotatedString(): AnnotatedString {
    if (this.isBlank()) return AnnotatedString("")

    // Final pass to handle remaining HTML tags like <b> or <i>
    val spanned = HtmlCompat.fromHtml(this, FROM_HTML_MODE_LEGACY)

    return buildAnnotatedString {
        append(spanned.toString().trim())
    }
}

fun String.toLanguageOrNull(): Language? {
    return Language.entries.firstOrNull { it.label.equals(this, ignoreCase = true) }
}