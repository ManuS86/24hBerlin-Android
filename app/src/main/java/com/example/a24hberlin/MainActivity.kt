package com.example.a24hberlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import com.example.a24hberlin.data.model.AppUser
import com.example.a24hberlin.ui.screens.auth.AuthWrapper
import com.example.a24hberlin.ui.theme._24hBerlinTheme
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val eventVM: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _24hBerlinTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Image(
                        painterResource(R.drawable.background),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    AuthWrapper(innerPadding)
                }
            }
        }
        observeUserStateAndPermissions()
    }

    override fun onResume() {
        super.onResume()
        observeUserStateAndPermissions()
    }

    private fun observeUserStateAndPermissions() {
        lifecycleScope.launch {
            combine(
                eventVM.currentAppUser,
                eventVM.hasNotificationPermission
            ) { user, hasNotificationPermission ->
                Pair(user, hasNotificationPermission)
            }.collectLatest { (user, hasNotificationPermission) ->
                checkAndScheduleReminder(user, hasNotificationPermission)
            }
        }
    }

    private fun checkAndScheduleReminder(user: AppUser?, hasNotificationPermission: Boolean) {
        if (user != null && hasNotificationPermission) {
            eventVM.setupAbsenceReminder()
        }
    }
}