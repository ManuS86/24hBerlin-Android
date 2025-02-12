package com.example.a24hberlin.ui.screens.appnavigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables.MyBottomNavigationBar
import com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables.MyTopAppBar
import com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables.NavGraph

const val EVENTS_ROUTE = "events"
const val CLUB_MAP_ROUTE = "club_map"
const val FAVORITES_ROUTE = "favorites"
const val SETTINGS_ROUTE = "settings"
const val FORGOT_PASSWORD_ROUTE = "forgot_password"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var appBarTitle by remember { mutableStateOf("Events") }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = appBarTitle,
                showSearchBar = showSearchBar,
                searchText = searchText,
                onSearchIconClick = { showSearchBar = !showSearchBar },
                onSearchTextChanged = { searchText = it },
                onSearchClosed = { showSearchBar = false }
            )
        },
        bottomBar = {
            MyBottomNavigationBar(
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
            NavGraph(navController, searchText)
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true, showSystemUi = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}