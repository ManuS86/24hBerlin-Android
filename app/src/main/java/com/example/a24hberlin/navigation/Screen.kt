package com.example.a24hberlin.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.a24hberlin.R

sealed class Screen(
    val route: String,
    @get:StringRes val titleResId: Int? = null
) {
    // --- Primary Bottom Tabs ---
    data object Events : Screen("events", R.string.app_name)
    data object ClubMap : Screen("club_map", R.string.app_name)
    data object MyEvents : Screen("my_events", R.string.app_name)
    data object Settings : Screen("settings", R.string.app_name)

    // --- Nested/Utility Screens ---
    data object ReAuthWrapper : Screen("reauth_wrapper/{from}", R.string.re_authenticate) {
        const val ARG_FROM = "from"

        val navArguments = listOf(
            navArgument(ARG_FROM) { type = NavType.StringType }
        )

        fun createRoute(from: String): String = "reauth_wrapper/$from"
    }
}