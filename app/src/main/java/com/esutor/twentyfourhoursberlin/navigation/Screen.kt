package com.esutor.twentyfourhoursberlin.navigation

import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.esutor.twentyfourhoursberlin.R

@Keep
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
    data object ReAuthWrapper : Screen("re_auth_wrapper/{from}", R.string.re_authenticate) {
        const val ARG_FROM = "from"

        val navArguments = listOf(
            navArgument(ARG_FROM) { type = NavType.StringType }
        )

        fun createRoute(from: String): String = "re_auth_wrapper/$from"
    }

    companion object {
        val allScreens: List<Screen>
            get() = Screen::class.sealedSubclasses.mapNotNull { it.objectInstance }

        fun fromRoute(route: String?): Screen? {
            if (route == null) return null
            val cleanPath = route.substringBefore("/")
            return allScreens.find { it.route.substringBefore("/") == cleanPath }
        }
    }
}