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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.wyattconrad.cs_360weighttracker.ui.components.FilterButton
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
    var filter by rememberSaveable { mutableStateOf(ChartFilter.Last30) }
    val filteredWeights = remember(weights, filter) {
        viewModel.filterWeights(weights, filter)
    }


    // Scaffold with floating action button
    Scaffold(

    ) { padding ->
        // Display the weights and goal state in a column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Add the goal section and weight summary section
            // to the column
            GoalSection(goalState, reachGoalDate)

            Spacer(modifier = Modifier.height(16.dp))

            // Display the weight change and weight to goal
            WeightSummarySection(weightChange, weightToGoal)

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            // Add a floating action button to add a new weight
            FloatingActionButton(
                onClick = { viewModel.setShowWeightSheet(true) },
                containerColor = Color(0xFF4CAF50),
                modifier = Modifier.align(Alignment.End).padding(bottom = 16.dp),
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Weight")
                }
            )
            
            // Only show chart if there are logged weights
            if (weights.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Add the filter buttons
                    FilterButton("This Month", ChartFilter.CurrentMonth, filter) { filter = it }
                    FilterButton("Last 30", ChartFilter.Last30, filter) { filter = it }
                    FilterButton("Last 100", ChartFilter.Last100, filter) { filter = it }
                    FilterButton("All", ChartFilter.All, filter) { filter = it }
                }

                // Display the weight chart
                WeightLineChart(
                    weights = filteredWeights,
                    trendValues = trendData.trendValues,
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )
            } else {
                // Display no data yet when there are no weights
                Text("No data yet")
            }

            // Display the weight input bottom sheet if it is visible
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