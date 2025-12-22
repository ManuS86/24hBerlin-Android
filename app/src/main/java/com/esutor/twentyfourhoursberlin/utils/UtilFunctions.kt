package com.esutor.twentyfourhoursberlin.utils

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.esutor.twentyfourhoursberlin.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.core.graphics.createBitmap

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null

    val width = drawable.intrinsicWidth.coerceAtLeast(1)
    val height = drawable.intrinsicHeight.coerceAtLeast(1)

    drawable.setBounds(0, 0, width, height)

    val bm = createBitmap(width, height)
    val canvas = Canvas(bm)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bm)
}

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