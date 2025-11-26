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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wyattconrad.cs_360weighttracker.service.roundTo2
import com.wyattconrad.cs_360weighttracker.ui.theme.AppTheme

@Composable
fun WeightChangeCard(
    weightChange: Double,
    modifier: Modifier = Modifier
) {
    // Determine the status and corresponding text based on the value
    val (statusText, statusColor) = when {
        weightChange > 0.0 -> {
            // Positive change: Loss of weight
            "Weight Lost:" to Color(0xFF4CAF50)
        }
        weightChange < 0.0 -> {
            // Negative change: Gain of weight
            "Weight Gained:" to Color(0xFFF44336)
        }
        else -> {
            // No change
            "Net Change:" to Color(0xFFFFFFFF)
        }
    }

    // Use the absolute value for display, as the text label handles the sign
    val displayValue = kotlin.math.abs(weightChange.roundTo2()).toString()

    // Weight change card
    Card(
        modifier = modifier.background(Color.Transparent)
    ) {
        // Display the weight change and weight to goal in a column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 2.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text views in the box
            Text(statusText, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(displayValue, fontSize = 32.sp, color = statusColor, fontWeight = FontWeight.Bold)
            Text("lbs.", color = statusColor)
        }
    }
}

// Preview for the WeightChangeCard composable
@Preview(showBackground = true)
@Composable
fun WeightChangeCardPreview() {
    WeightChangeCard(weightChange = 10.0)
}

// Preview for the WeightChangeCard composable
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeightChangeCardPreview2() {
    AppTheme(dynamicColor = false) {
        WeightChangeCard(weightChange = 10.0)
    }
}