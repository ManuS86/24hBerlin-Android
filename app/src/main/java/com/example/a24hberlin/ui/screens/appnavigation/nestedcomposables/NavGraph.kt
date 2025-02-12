package com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a24hberlin.ui.screens.appnavigation.CLUB_MAP_ROUTE
import com.example.a24hberlin.ui.screens.appnavigation.EVENTS_ROUTE
import com.example.a24hberlin.ui.screens.appnavigation.FAVORITES_ROUTE
import com.example.a24hberlin.ui.screens.appnavigation.FORGOT_PASSWORD_ROUTE
import com.example.a24hberlin.ui.screens.appnavigation.SETTINGS_ROUTE
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.ForgotPasswordScreen
import com.example.a24hberlin.ui.screens.clubmap.ClubMapScreen
import com.example.a24hberlin.ui.screens.events.EventsScreen
import com.example.a24hberlin.ui.screens.favorites.FavoritesScreen
import com.example.a24hberlin.ui.screens.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController, searchText: TextFieldValue) {
    NavHost(navController = navController, startDestination = EVENTS_ROUTE) {
        composable(EVENTS_ROUTE) {
            EventsScreen(searchText)
        }
        composable(CLUB_MAP_ROUTE) {
            ClubMapScreen(searchText)
        }
        composable(FAVORITES_ROUTE) {
            FavoritesScreen(searchText)
        }
        composable(SETTINGS_ROUTE) {
            SettingsScreen()
        }
        composable(FORGOT_PASSWORD_ROUTE) {
            ForgotPasswordScreen()
        }
    }
}