package com.example.a24hberlin.ui.screens.components.utilityelements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a24hberlin.ui.viewmodel.EventViewModel

@Composable
fun ScheduleReminderEffect(eventVM: EventViewModel) {
    val user = eventVM.currentAppUser.collectAsStateWithLifecycle(initialValue = null).value
    val hasPermission =
        eventVM.hasNotificationPermission.collectAsStateWithLifecycle(initialValue = false).value

    LaunchedEffect(user, hasPermission) {
        if (user != null && hasPermission) {
            eventVM.setupAbsenceReminder()
        }
    }
}