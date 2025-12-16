package com.example.a24hberlin.navigation

import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.a24hberlin.R

sealed class Screen(
    val route: String,
    @get:StringRes val titleResId: Int? = null,
    val arguments: List<NamedNavArgument> = emptyList()) {

    // --- Primary Bottom Tabs ---
    data object Events : Screen("events", R.string.events)
    data object ClubMap : Screen("club_map", R.string.club_map)
    data object Favorites : Screen("favorites", R.string.favorite)
    data object Settings : Screen("settings", R.string.settings)

    // --- Nested/Utility Screens ---
    data class ReAuthWrapper(val from: String) :
        Screen(
            ROUTE,
            R.string.re_authenticate,
            arguments
        ) {

        companion object {
            const val ROUTE = "reauth_wrapper/{from}"
            const val ARGUMENT_KEY = "from"

            val arguments = listOf(
                navArgument(ARGUMENT_KEY) { type = NavType.StringType }
            )

            fun createRoute(from: String): String = "reauth_wrapper/$from"

            val route: String = ROUTE
        }
    }
}