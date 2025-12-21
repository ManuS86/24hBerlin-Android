package com.example.a24hberlin.ui.screens.mainhost.nestedcomposables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a24hberlin.navigation.navItemsData

@Composable
fun MyBottomNavigationBar(
    navController: NavHostController,
    onTabSelected: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val items = navItemsData

    NavigationBar(
        containerColor = Black,
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route
            val label = stringResource(id = item.labelResId)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        haptic.performHapticFeedback(TextHandleMove)
                        onTabSelected()
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = label,
                        modifier = Modifier.offset(y = 6.dp)
                    )
                },
                modifier = Modifier
                    .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                    .height(48.dp),
                label = { Text(
                    text = label,
                    style = typography.labelSmall
                ) },
                alwaysShowLabel = true,
                colors = colors(
                    selectedIconColor = White,
                    selectedTextColor = White,
                    unselectedIconColor = Gray,
                    unselectedTextColor = Gray,
                    indicatorColor = Transparent
                )
            )
        }
    }
}