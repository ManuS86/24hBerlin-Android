package com.example.a24hberlin.utils

import android.text.Spanned
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.theme.ArtAndCulture
import com.example.a24hberlin.ui.theme.Concert
import com.example.a24hberlin.ui.theme.Party

fun Event.getEventColor(): Color {
    val eventTypes = this.eventType?.values ?: emptySet()

    fun String.isMatch(category: String) =
        this.replace("&amp;", "&").equals(category, ignoreCase = true)

    return when {
        eventTypes.any { it.isMatch("Konzert") } -> Concert
        eventTypes.any { it.isMatch("Party") } -> Party
        eventTypes.any { it.isMatch("Kunst & Kultur") } -> ArtAndCulture
        else -> Party
    }
}

@DrawableRes
fun Event.getMarkerResourceId(): Int {
    val eventTypes = this.eventType?.values ?: emptySet()

    fun String.isMatch(category: String) =
        this.replace("&amp;", "&").equals(category, ignoreCase = true)

    return when {
        eventTypes.any { it.isMatch("Konzert") } -> R.drawable.ic_concert_location
        eventTypes.any { it.isMatch("Party") } -> R.drawable.ic_party_location
        eventTypes.any { it.isMatch("Kunst & Kultur") } -> R.drawable.ic_art_and_culture_location
        else -> R.drawable.ic_party_location
    }
}

fun String.cleanToAnnotatedString(): AnnotatedString {
    if (this.isBlank()) return AnnotatedString("")

    // 1. Initial decode
    val firstPass = HtmlCompat.fromHtml(this, FROM_HTML_MODE_LEGACY).toString()

    // 2. RESCUE JS content
    val jsContentRegex = Regex("`([^`]+)`")
    val extractedContent = jsContentRegex.find(firstPass)?.groupValues?.get(1) ?: ""

    // 3. Remove JS boilerplate
    val noJS = firstPass.replace(
        Regex(
            """document\.getElementById\(.*?\)\.innerHTML\s*=\s*[`'"].*?[`'"]\s*;""",
            RegexOption.DOT_MATCHES_ALL
        ),
        ""
    ).trim()

    // 4. Combine ONLY if extractedContent is not empty
    val combinedHtml = if (extractedContent.isNotBlank()) {
        "$noJS<br><br>$extractedContent"
    } else {
        noJS
    }

    // 5. Convert to Spanned
    val spanned: Spanned = HtmlCompat.fromHtml(
        combinedHtml,
        FROM_HTML_MODE_LEGACY
    )

    // 6. TRIM THE RESULT and convert to AnnotatedString
    // We use .toString().trim() to remove those pesky trailing \n characters
    return buildAnnotatedString {
        append(spanned.toString().trim())
    }
}

fun String.toLanguageOrNull(): Language? {
    return Language.entries.firstOrNull { it.label.equals(this, ignoreCase = true) }
}