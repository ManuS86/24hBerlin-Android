package com.example.a24hberlin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a24hberlin.ui.screens.auth.nestedcomposables.ForgotPasswordScreen
import com.example.a24hberlin.ui.screens.clubmap.ClubMapScreen
import com.example.a24hberlin.ui.screens.events.EventsScreen
import com.example.a24hberlin.ui.screens.favorites.FavoritesScreen
import com.example.a24hberlin.ui.screens.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController, searchText: TextFieldValue) {
    NavHost(
        navController = navController,
        startDestination = Screen.Events.route
    ) {
        composable(Screen.Events.route) {
            EventsScreen(searchText)
        }
        composable(Screen.ClubMap.route) {
            ClubMapScreen(searchText)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(searchText)
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen()
        }
    }
}