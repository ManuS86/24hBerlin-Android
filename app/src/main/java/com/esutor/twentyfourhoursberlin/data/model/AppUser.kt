package com.esutor.twentyfourhoursberlin.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import java.util.Date

@Keep
data class AppUser(
    var bookmarkIDs: MutableList<String> = mutableListOf(),
    var registerDate: Timestamp = Timestamp(Date()),
    var settings: Settings = Settings()
)
