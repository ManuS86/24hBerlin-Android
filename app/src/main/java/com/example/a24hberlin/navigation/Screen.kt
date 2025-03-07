package com.example.a24hberlin.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    data object Events : Screen("events")
    data object ClubMap : Screen("club_map")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")
    data class ReAuthWrapper(val from: String) :
        Screen("reauth_wrapper/{from}", listOf(navArgument("from") { type = NavType.StringType })) {
        fun createRoute(from: String) = "reauth_wrapper/$from"
    }
}