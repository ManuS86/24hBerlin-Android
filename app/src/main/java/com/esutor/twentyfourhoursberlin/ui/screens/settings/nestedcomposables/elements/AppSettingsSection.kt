package com.esutor.twentyfourhoursberlin.ui.screens.settings.nestedcomposables.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.enums.Language
import com.esutor.twentyfourhoursberlin.data.model.Event
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilityelements.LanguageDropdown
import com.esutor.twentyfourhoursberlin.ui.theme.regularPadding
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding
import com.esutor.twentyfourhoursberlin.ui.theme.microPadding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel
import com.esutor.twentyfourhoursberlin.ui.viewmodel.SettingsViewModel
import kotlin.collections.forEach

@Composable
fun AppSettingsSection(
    language: Language?,
    notificationsEnabled: Boolean,
    bookmarks: List<Event>,
    settingsVM: SettingsViewModel,
    eventVM: EventViewModel
) {
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = slightRounding,
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = slightRounding,
        colors = cardColors(
            containerColor = White,
            contentColor = Black
        )
    ) {
        // Language Settings
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = regularPadding),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(R.string.language_settings),
                modifier = Modifier.padding(vertical = regularPadding)
            )

            Spacer(Modifier.weight(1f))

            LanguageDropdown(
                label = stringResource(R.string.system_default),
                selectedValue = language,
                onValueSelected = { settingsVM.changeLanguage(it) },
                options = Language.entries.toList()
            )
        }
    }

    Spacer(Modifier.padding(microPadding))

    // Notifications
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = slightRounding,
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = slightRounding,
        colors = cardColors(
            containerColor = White,
            contentColor = Black
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = regularPadding),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(R.string.notifications),
                modifier = Modifier.padding(vertical = regularPadding)
            )

            Spacer(Modifier.weight(1f))

            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { isChecked ->
                    haptic.performHapticFeedback(TextHandleMove)
                    settingsVM.changeNotificationPermission(isChecked)
                    if (isChecked) {
                        bookmarks.forEach { bookmark ->
                            eventVM.addBookmarkReminder(bookmark)
                        }
                        eventVM.setupAbsenceReminder()
                    } else {
                        settingsVM.cancelAllReminders(bookmarks)
                    }
                },
                colors = SwitchDefaults.colors(uncheckedTrackColor = LightGray.copy(0.5f))
            )
        }
    }
}