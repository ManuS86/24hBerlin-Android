package com.example.a24hberlin.ui.screens.settings.nestedcomposables.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.TextHandleMove
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.data.model.Event
import com.example.a24hberlin.ui.screens.components.utilityelements.LanguageDropdown
import com.example.a24hberlin.ui.theme.regularPadding
import com.example.a24hberlin.ui.theme.slightRounding
import com.example.a24hberlin.ui.theme.microPadding
import com.example.a24hberlin.ui.viewmodel.EventViewModel
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import kotlin.collections.forEach

@Composable
fun AppSettingsSection(
    language: Language?,
    pushNotificationsEnabled: Boolean,
    bookmarks: List<Event>,
    haptic: HapticFeedback,
    eventVM: EventViewModel = viewModel(),
    settingsVM: SettingsViewModel = viewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(slightRounding),
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = RoundedCornerShape(slightRounding),
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

    // Push Notifications
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(slightRounding),
                ambientColor = Gray.copy(0.5f),
                spotColor = Gray.copy(0.5f)
            ),
        shape = RoundedCornerShape(slightRounding),
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
                text = stringResource(R.string.push_notifications),
                modifier = Modifier.padding(vertical = regularPadding)
            )

            Spacer(Modifier.weight(1f))

            Switch(
                checked = pushNotificationsEnabled,
                onCheckedChange = { isChecked ->
                    haptic.performHapticFeedback(TextHandleMove)
                    settingsVM.changePushNotifications(isChecked)
                    if (isChecked) {
                        bookmarks.forEach { bookmark ->
                            eventVM.addBookmarkPushNotifications(bookmark)
                        }
                        eventVM.setupAbsenceReminder()
                    } else {
                        settingsVM.removeAllPendingNotifications(bookmarks)
                    }
                },
                colors = SwitchDefaults.colors(uncheckedTrackColor = LightGray.copy(0.5f))
            )
        }
    }
}