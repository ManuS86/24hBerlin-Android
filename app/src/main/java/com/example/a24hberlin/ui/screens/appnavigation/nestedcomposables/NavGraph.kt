package com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a24hberlin.ui.screens.clubmap.ClubMapScreen
import com.example.a24hberlin.ui.screens.events.EventsScreen
import com.example.a24hberlin.ui.screens.favorites.FavoritesScreen
import com.example.a24hberlin.ui.screens.settings.SettingsScreen

const val EVENTS_ROUTE = "events"
const val CLUB_MAP_ROUTE = "club map"
const val FAVORITES_ROUTE = "favorites"
const val SETTINGS_ROUTE = "settings"

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = EVENTS_ROUTE) {
        composable(EVENTS_ROUTE) { EventsScreen() }
        composable(CLUB_MAP_ROUTE) { ClubMapScreen() }
        composable(FAVORITES_ROUTE) { FavoritesScreen() }
        composable(SETTINGS_ROUTE) { SettingsScreen() }
    }
}