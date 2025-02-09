package com.example.a24hberlin.ui.screens.appnavigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables.BottomNavigationBar
import com.example.a24hberlin.ui.screens.clubmap.ClubMapScreen
import com.example.a24hberlin.ui.screens.components.utilitybars.SearchBar
import com.example.a24hberlin.ui.screens.events.EventsScreen
import com.example.a24hberlin.ui.screens.favorites.FavoritesScreen
import com.example.a24hberlin.ui.screens.settings.SettingsScreen

const val EVENTS_ROUTE = "events"
const val CLUB_MAP_ROUTE = "club_map"
const val FAVORITES_ROUTE = "favorites"
const val SETTINGS_ROUTE = "settings"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var appBarTitle by remember { mutableStateOf("Events") }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        appBarTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    if (appBarTitle != "Settings")
                        if (!showSearchBar) {
                            IconButton(onClick = { showSearchBar = !showSearchBar }) {
                                Icon(Icons.Filled.Search, contentDescription = "Search")
                            }
                        } else {
                            SearchBar(
                                searchText,
                                { searchText = it },
                                { showSearchBar = false }
                            )
                        }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController,
                onTitleChange = {
                    appBarTitle = it
                    showSearchBar = false
                    searchText = TextFieldValue("")
                }
            )
        }
    ) { paddingValues ->
        Surface(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
            }
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true, showSystemUi = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}