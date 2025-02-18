package com.example.a24hberlin.ui.screens.appnavigation

import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.data.enums.Sound
import com.example.a24hberlin.navigation.NavGraph
import com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables.MyBottomNavigationBar
import com.example.a24hberlin.ui.screens.appnavigation.nestedcomposables.MyTopAppBar
import com.example.a24hberlin.ui.screens.components.utilitybars.FilterBar
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.utils.SetSystemBarColorsToLight

@Composable
fun AppNavigation() {
    val eventVM: EventViewModel = viewModel()
    val navController = rememberNavController()
    var appBarTitle by remember { mutableStateOf("Events") }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedEventType by remember { mutableStateOf<EventType?>(null) }
    var selectedMonth by remember { mutableStateOf<Month?>(null) }
    var selectedSound by remember { mutableStateOf<Sound?>(null) }
    var selectedVenue by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Column {
                MyTopAppBar(
                    title = appBarTitle,
                    showSearchBar = showSearchBar,
                    searchText = searchText,
                    onSearchIconClick = { showSearchBar = !showSearchBar },
                    onSearchTextChanged = { searchText = it },
                    onSearchClosed = { showSearchBar = false }
                )

                if (appBarTitle != "Settings") {
                    FilterBar(
                        selectedEventType = selectedEventType,
                        onEventTypeSelected = { selectedEventType = it },
                        selectedMonth = selectedMonth,
                        onMonthSelected = { selectedMonth = it },
                        selectedSound = selectedSound,
                        onSoundSelected = { selectedSound = it },
                        selectedVenue = selectedVenue,
                        onVenueSelected = { selectedVenue = it },
                        venues = eventVM.uniqueLocations
                    )
                }
            }
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
            SetSystemBarColorsToLight(false)
            NavGraph(navController, searchText)
        }
    }
}