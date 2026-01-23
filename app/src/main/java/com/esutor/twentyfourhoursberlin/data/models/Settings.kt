package com.esutor.twentyfourhoursberlin.data.models

import androidx.annotation.Keep

@Keep
data class Settings(
    var notificationsEnabled: Boolean = true,
    var language: String? = null
)
