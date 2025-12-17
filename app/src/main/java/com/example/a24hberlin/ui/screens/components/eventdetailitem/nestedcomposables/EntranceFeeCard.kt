package com.example.a24hberlin.ui.screens.components.eventdetailitem.nestedcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.a24hberlin.R
import com.example.a24hberlin.data.model.EntranceFee
import com.example.a24hberlin.ui.theme.Details
import com.example.a24hberlin.ui.theme.Party
import com.example.a24hberlin.utils.mediumPadding
import com.example.a24hberlin.utils.mediumRounding
import com.example.a24hberlin.utils.regularPadding

@Composable
fun EntranceFeeCard(entranceFee: EntranceFee?) {
    entranceFee?.let {
        val regex = remember { "<.*?>".toRegex() }
        val cleanedEntranceFee = regex.replace(entranceFee.value.replace("<br>", "\n"), "")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(mediumRounding),
            colors = CardDefaults.cardColors(
                containerColor = Details
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(regularPadding)
            ) {
                Icon(
                    Icons.Filled.Payments,
                    contentDescription = null,
                    modifier = Modifier.padding(end = mediumPadding),
                    tint = Party
                )

                Column {
                    Text(
                        text = stringResource(R.string.entrance_fee),
                        modifier = Modifier.padding(bottom = mediumPadding),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    SelectionContainer {
                        Text(
                            text = cleanedEntranceFee,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}