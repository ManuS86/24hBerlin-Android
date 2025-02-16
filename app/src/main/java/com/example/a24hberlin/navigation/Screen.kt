package com.example.a24hberlin.navigation

sealed class Screen(val route: String) {
    data object Events : Screen("events")
    data object ClubMap : Screen("club map")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")
    data object ForgotPassword : Screen("forgot password")

//    data class EventDetails(val eventId: Int) : Screen("event_details/{eventId}") {
//        fun createRoute(eventId: Int) = "event_details/$eventId"
//    }
}