/*
 * Copyright (C) 2025 Wyatt Conrad. All rights reserved.
 *
 * This file is part of the CS-360 Weight Tracker project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wyattconrad.cs_360weighttracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toKotlinLocalDateTime

/**
 * Composable for displaying a single weight item in the weight list.
 *
 * @param weight The Weight object to display.
 * @param onEditClick Lambda to be invoked when the edit button is clicked.
 * @param onDeleteClick Lambda to be invoked when the delete button is clicked.
 * @param modifier Modifier for styling and layout.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun WeightItem(
    weight: Weight,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Card for the weight item
    Card(modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        // Row for the weight and actions
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            // Weight and Date section
            Column(modifier = Modifier.weight(1f)) {
                // Weight text
                Text(
                    text = "${weight.weight} lbs",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                // Date text
                Text(
                    text = weight.dateTimeLogged.toKotlinLocalDateTime().format(LocalDateTime.Format {
                        monthNumber(); char('/'); dayOfMonth()
                        char('/'); year()
                        char(' ')
                        amPmHour(); char(':'); minute()
                        amPmMarker("AM", "PM")
                    }),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Spacer to push icons to the right
            Spacer(modifier = Modifier.width(16.dp))

            // Icon buttons for actions
            Row {
                // Edit button
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Weight",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                // Delete button
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Weight",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

// Preview for the WeightItem composable
@Preview(showBackground = true)
@Composable
fun WeightItemPreview() {
    // Create a fake Weight object for the preview.
    val sampleWeight = Weight(
        185.5,
        1L
    )

    // Call WeightItem with the sample data and empty lambdas for the clicks.
    WeightItem(
        weight = sampleWeight,
        onEditClick = { /* Clicks do nothing in preview */ },
        onDeleteClick = { /* Clicks do nothing in preview */ },
        modifier = Modifier.padding(16.dp)
    )
}