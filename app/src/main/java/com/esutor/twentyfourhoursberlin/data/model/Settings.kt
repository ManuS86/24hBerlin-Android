package com.esutor.twentyfourhoursberlin.data.model

data class Settings(
    var pushNotificationsEnabled: Boolean = true,
    var language: String? = null
)
