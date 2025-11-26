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
package com.wyattconrad.cs_360weighttracker.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.wyattconrad.cs_360weighttracker.ui.components.GoalSection
import com.wyattconrad.cs_360weighttracker.ui.components.WeightInputBottomSheet
import com.wyattconrad.cs_360weighttracker.ui.components.WeightLineChart
import com.wyattconrad.cs_360weighttracker.ui.components.WeightSummarySection
import java.time.Instant

/**
 * The main screen for the Weight Tracker app.
 * @param viewModel The view model for this screen.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {

    // Initialize the state values
    val showWeightSheet by viewModel.showWeightSheet.collectAsState()
    val goalState by viewModel.goalState.collectAsState()
    val weights by viewModel.weights.collectAsState(emptyList())
    val weightChange by viewModel.weightChange.collectAsState()
    val weightToGoal by viewModel.weightToGoal.collectAsState()
    val trendData by viewModel.trendData.collectAsState()
    val reachGoalDate by viewModel.estimatedGoalDate.collectAsState()

    // Scaffold with floating action button
    Scaffold(
        // Add a floating action button for adding new weights
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowWeightSheet(true) },
                containerColor = Color(0xFF4CAF50),
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Weight")
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        // Display the weights and goal state in a column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            GoalSection(goalState, reachGoalDate)

            Spacer(modifier = Modifier.height(16.dp))

            // Display the weight change and weight to goal
            WeightSummarySection(weightChange, weightToGoal)

            Spacer(
                modifier = Modifier
                    .height(64.dp)
            )

            // Only show chart if there is data
            if (trendData.trendValues.isNotEmpty()) {
                WeightLineChart(
                    weights = weights,
                    trendValues = trendData.trendValues,
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )
            } else {
                Text("No data yet")
            }

            WeightInputBottomSheet(
                isVisible = showWeightSheet,
                onDismiss = { viewModel.setShowWeightSheet(false) },
                onSaveWeight = viewModel::onSaveWeight,
                inputWeight = 0.0,
                selectedDateTime = Instant.now()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}