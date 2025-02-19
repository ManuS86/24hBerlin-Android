package com.example.a24hberlin.ui.screens.settings

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a24hberlin.R
import com.example.a24hberlin.data.enums.Language
import com.example.a24hberlin.ui.screens.components.utilityelements.LanguageDropdown
import com.example.a24hberlin.ui.viewmodel.SettingsViewModel
import com.example.a24hberlin.utils.extraLargePadding
import com.example.a24hberlin.utils.logoSizeSmall
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.regularPadding
import com.example.a24hberlin.utils.slightRounding
import com.example.a24hberlin.utils.smallPadding

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsVM: SettingsViewModel = viewModel()
    val scrollState = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(regularPadding)
        ) {
            Text(
                "Account Details",
                Modifier.padding(bottom = smallPadding),
                fontWeight = FontWeight.Medium
            )

            Card(
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(slightRounding),
                        ambientColor = Color.Gray.copy(0.5f),
                        spotColor = Color.Gray.copy(0.5f)
                    ),
                shape = RoundedCornerShape(slightRounding),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(regularPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.change_email))

                    Spacer(Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        tint = Color.Gray,
                        contentDescription = stringResource(R.string.change_email),
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {

                            }
                    )
                }

                HorizontalDivider(
                    Modifier
                        .padding(horizontal = regularPadding),
                    color = Color.LightGray
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(regularPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.change_password))

                    Spacer(Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        tint = Color.Gray,
                        contentDescription = stringResource(R.string.change_password),
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {

                            }
                    )
                }
            }

            Text(
                stringResource(R.string.app_settings),
                Modifier
                    .padding(top = regularPadding)
                    .padding(bottom = smallPadding),
                fontWeight = FontWeight.Medium
            )

            Card(
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(slightRounding),
                        ambientColor = Color.Gray.copy(0.5f),
                        spotColor = Color.Gray.copy(0.5f)
                    ),
                shape = RoundedCornerShape(slightRounding),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(regularPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.language_settings))

                    Spacer(Modifier.weight(1f))

                    LanguageDropdown(
                        label = stringResource(R.string.system_default),
                        selectedValue = settingsVM.language,
                        onValueSelected = { settingsVM.updateLanguage(it) },
                        options = Language.allValues.toList()
                    )
                }
            }

            Spacer(Modifier.padding(smallPadding))

            Card(
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(slightRounding),
                        ambientColor = Color.Gray.copy(0.5f),
                        spotColor = Color.Gray.copy(0.5f)
                    ),
                shape = RoundedCornerShape(slightRounding),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(regularPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.push_notifications))

                    Spacer(Modifier.weight(1f))

//                    Switch()
                }
            }

            Text(
                "Community",
                Modifier
                    .padding(top = regularPadding)
                    .padding(bottom = smallPadding),
                fontWeight = FontWeight.Medium
            )

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "https://play.google.com/store/apps/details?id=com.example.a24hberlin"
                        )
                    }
                    context.startActivity(
                        Intent.createChooser(
                            intent,
                            R.string.share_link.toString()
                        )
                    )
                },
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(slightRounding),
                        ambientColor = Color.Gray.copy(0.5f),
                        spotColor = Color.Gray.copy(0.5f)
                    ),
                shape = RoundedCornerShape(slightRounding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    stringResource(R.string.share_24hBerlin),
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = mediumPadding),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start
                )
            }

            Button(
                onClick = {

                },
                Modifier
                    .fillMaxWidth()
                    .padding(top = mediumPadding)
                    .padding(bottom = extraLargePadding)
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(slightRounding),
                        ambientColor = Color.Gray.copy(0.5f),
                        spotColor = Color.Gray.copy(0.5f)
                    ),
                shape = RoundedCornerShape(slightRounding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    stringResource(R.string.report_a_bug),
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = mediumPadding),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start
                )
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    settingsVM.logout()
                },
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(slightRounding),
                        ambientColor = Color.Gray.copy(0.5f),
                        spotColor = Color.Gray.copy(0.5f)
                    ),
                shape = RoundedCornerShape(slightRounding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    stringResource(R.string.logout),
                    Modifier.padding(mediumPadding),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                verticalArrangement = Arrangement.spacedBy(mediumPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    Modifier
                        .size(logoSizeSmall)
                )

                Text(
                    "Version 1.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Button(
                onClick = {
                    settingsVM.deleteAccount()
                },
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(slightRounding),
                        ambientColor = Color.Gray.copy(0.5f),
                        spotColor = Color.Gray.copy(0.5f)
                    ),
                shape = RoundedCornerShape(slightRounding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    stringResource(R.string.delete_account),
                    Modifier.padding(mediumPadding),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}