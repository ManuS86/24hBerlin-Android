package com.example.a24hberlin.ui.screens.settings.nestedcomposables.elements

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.a24hberlin.R
import com.example.a24hberlin.navigation.Screen
import com.example.a24hberlin.ui.theme.regularPadding
import com.example.a24hberlin.ui.theme.slightRounding

@Composable
fun AccountDetailsSection(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    haptic: HapticFeedback
) {
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
        // Change Email
        SettingsCardItem(
            title = stringResource(R.string.change_email),
            haptic = haptic,
            onClick = {
                bottomBarState.value = false
                navController.navigate(Screen.ReAuthWrapper.createRoute("email"))
            }
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = regularPadding),
            color = LightGray
        )

        // Change Password
        SettingsCardItem(
            title = stringResource(R.string.change_password),
            haptic = haptic,
            onClick = {
                bottomBarState.value = false
                navController.navigate(Screen.ReAuthWrapper.createRoute("password"))
            }
        )
    }
}

@Composable
private fun SettingsCardItem(
    title: String,
    haptic: HapticFeedback,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role = Role.Button,
                onClick = {
                    haptic.performHapticFeedback(TextHandleMove)
                    onClick()
                }
            )
            .padding(regularPadding),
        verticalAlignment = CenterVertically
    ) {
        Text(title)

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
            tint = Gray,
            contentDescription = title,
            modifier = Modifier
                .size(16.dp)
        )
    }
}