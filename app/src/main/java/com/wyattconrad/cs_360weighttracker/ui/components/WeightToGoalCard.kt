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

@Composable
fun WeightToGoalCard(
    weightToGoal: Double,
    modifier: Modifier = Modifier
) {
    // Determine the status and corresponding text based on the value
    val (statusText, statusColor) = when {
        weightToGoal > 0.0 -> {
            // Still have weight to lose to reach the goal
            "Remaining Goal:" to Color(0xFFFFFFFF)
        }
        else -> {
            // Goal reached or exceeded (0 or negative number)
            "Goal Reached!" to Color(0xFF4CAF50)
        }
    }

    // Use the absolute value for display if the goal hasn't been reached,
    // or display 0.00 if it has been met or exceeded.
    val displayValue = if (weightToGoal > 0.0) {
        weightToGoal.roundTo2().toString()
    } else {
        0.00.toString()
    }

    // Weight change card
    Card(
        modifier = modifier
    ) {
        // Display the weight change and weight to goal in a column
        Column(
            modifier = Modifier
                .background(Color(0xFF0075C4))
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text views in the box
            Text(statusText, color = Color.White)
            Text(displayValue, fontSize = 32.sp, color = statusColor, fontWeight = FontWeight.Bold)
            Text("lbs.", color = statusColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeightToGoalCardPreview() {
    WeightToGoalCard(weightToGoal = 10.0)
}

@Preview(showBackground = true)
@Composable
fun WeightToGoalCardPreview2() {
    WeightToGoalCard(weightToGoal = 0.0)
}

@Preview(showBackground = true)
@Composable
fun WeightToGoalCardPreview3() {
    WeightToGoalCard(weightToGoal = -3.75)
}