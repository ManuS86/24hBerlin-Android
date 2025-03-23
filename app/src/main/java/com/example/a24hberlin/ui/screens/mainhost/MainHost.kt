package com.example.a24hberlin.ui.screens.mainhost

import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.EventType
import com.example.a24hberlin.data.enums.Month
import com.example.a24hberlin.navigation.NavGraph
import com.example.a24hberlin.navigation.Screen
import com.example.a24hberlin.ui.screens.components.utilitybars.FilterBar
import com.example.a24hberlin.ui.screens.mainhost.nestedcomposables.MyBottomNavigationBar
import com.example.a24hberlin.ui.screens.mainhost.nestedcomposables.MyTopAppBar
import com.example.a24hberlin.utils.SetSystemBarColorsToLight

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val view = LocalView.current
    val navController = rememberNavController()
    var appBarTitle by remember { mutableStateOf("") }
    val bottomBarState = remember { mutableStateOf(true) }
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedEventType by remember { mutableStateOf<EventType?>(null) }
    var selectedMonth by remember { mutableStateOf<Month?>(null) }
    var selectedSound by remember { mutableStateOf<String?>(null) }
    var selectedVenue by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Column {
                MyTopAppBar(
                    title = appBarTitle,
                    showSearchBar = showSearchBar,
                    searchText = searchText,
                    onSearchIconClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        showSearchBar = !showSearchBar
                    },
                    onSearchTextChanged = { searchText = it },
                    onSearchClosed = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        showSearchBar = false
                    },
                    navController = navController
                )

                if (
                    appBarTitle != stringResource(R.string.settings) &&
                    appBarTitle != stringResource(R.string.re_authenticate) &&
                    appBarTitle != stringResource(R.string.change_email) &&
                    appBarTitle != stringResource(R.string.change_password)
                ) {
                    FilterBar(
                        selectedEventType = selectedEventType,
                        onEventTypeSelected = { selectedEventType = it },
                        selectedMonth = selectedMonth,
                        onMonthSelected = { selectedMonth = it },
                        selectedSound = selectedSound,
                        onSoundSelected = { selectedSound = it },
                        selectedVenue = selectedVenue,
                        onVenueSelected = { selectedVenue = it }
                    )
                }
            }
        },
        bottomBar = {
            if (bottomBarState.value) {
                MyBottomNavigationBar(
                    navController,
                    onTitleChange = {
                        appBarTitle = it
                        showSearchBar = false
                        searchText = TextFieldValue("")
                    }
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        Surface(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            SetSystemBarColorsToLight(false)
            NavGraph(
                navController,
                searchText,
                selectedEventType,
                selectedMonth,
                selectedSound,
                selectedVenue,
                bottomBarState,
            ) { appBarTitle = it }
        }
    }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            appBarTitle = when (destination.route) {
                "events" -> context.getString(R.string.events)
                "club_map" -> context.getString(R.string.club_map)
                "favorites" -> context.getString(R.string.my_events)
                "settings" -> context.getString(R.string.settings)
                else -> context.getString(R.string.re_authenticate)
            }

            bottomBarState.value = when (destination.route) {
                Screen.Settings.route -> true
                Screen.ReAuthWrapper("").route -> false
                else -> bottomBarState.value
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}