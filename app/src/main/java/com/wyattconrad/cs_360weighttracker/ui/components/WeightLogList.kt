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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wyattconrad.cs_360weighttracker.model.Weight

/**
 * A composable that displays a list of weights.
 *
 * @param weights The list of weights to display.
 * @param onEditClick A lambda that is called when the edit button is clicked.
 * @param onDeleteClick A lambda that is called when the delete button is clicked.
 * @param modifier The modifier to apply to this composable.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun WeightLogList(
    weights: List<Weight>,
    onEditClick: (Weight) -> Unit,
    onDeleteClick: (Weight) -> Unit,
    modifier: Modifier = Modifier
) {
    // Create a LazyColumn to display the weights
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Iterate through the weights and display each one
        items(
            items = weights,
            key = { it.id }
        ) { weight ->
            // Create a WeightItem composable for each weight
            WeightItem(
                weight = weight,
                onEditClick = { onEditClick(weight) },
                onDeleteClick = { onDeleteClick(weight) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

// Preview the WeightLogList composable
@Preview(showBackground = true)
@Composable
fun WeightListPreview() {
    WeightLogList(
        weights = listOf(
            Weight(weight = 150.0, userId = 1),
            Weight(weight = 149.0, userId = 1)
        ),
        onEditClick = {},
        onDeleteClick = {}
    )
}