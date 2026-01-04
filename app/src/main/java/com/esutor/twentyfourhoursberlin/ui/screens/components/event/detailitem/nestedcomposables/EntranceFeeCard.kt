package com.esutor.twentyfourhoursberlin.ui.screens.components.event.detailitem.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.data.model.EntranceFee
import com.esutor.twentyfourhoursberlin.ui.theme.OffWhite
import com.esutor.twentyfourhoursberlin.ui.theme.Party
import com.esutor.twentyfourhoursberlin.utils.cleanToAnnotatedString
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.mediumRounding
import com.esutor.twentyfourhoursberlin.ui.theme.standardPadding

@Composable
fun EntranceFeeCard(entranceFee: EntranceFee?) {
    entranceFee?.let {
        val cleanedEntranceFee = remember(entranceFee) {
            entranceFee.value.cleanToAnnotatedString()
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = mediumRounding,
            colors = cardColors(
                containerColor = OffWhite
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(standardPadding)
            ) {
                Icon(
                    Icons.Filled.Payments,
                    contentDescription = null,
                    modifier = Modifier.padding(end = smallPadding),
                    tint = Party
                )

                Column {
                    Text(
                        text = stringResource(R.string.entrance_fee),
                        modifier = Modifier.padding(bottom = smallPadding),
                        style = typography.titleMedium,
                        fontWeight = Bold
                    )

                    SelectionContainer {
                        Text(
                            text = cleanedEntranceFee,
                            style = typography.bodyLarge,
                            color = DarkGray
                        )
                    }
                }
            }
        }
    }
}