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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.wyattconrad.cs_360weighttracker.ui.components.GoalText
import com.wyattconrad.cs_360weighttracker.ui.components.WeightChangeCard
import com.wyattconrad.cs_360weighttracker.ui.components.WeightInputBottomSheet
import com.wyattconrad.cs_360weighttracker.ui.components.WeightLineChart
import com.wyattconrad.cs_360weighttracker.ui.components.WeightToGoalCard
import com.wyattconrad.cs_360weighttracker.utilities.TrendAnalysis
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

    // State for snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // State to control the bottom sheet visibility
    var showWeightSheet by rememberSaveable { mutableStateOf(false) }

    // State method to save the updated weight
    val onSaveWeight: (Double, Instant) -> Unit = { weight, dateTime ->
        viewModel.addWeight(weight, dateTime)
        showWeightSheet = false
    }

    // Establish a date formatter
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    // Collect the weight and goal state from the view model
    val weights by viewModel.weights.collectAsState(initial = emptyList())
    val weightChange by viewModel.weightChange.collectAsState(initial = 0.00)
    val weightToGoal by viewModel.weightToGoal.collectAsState(initial = 0.00)
    val goalState by viewModel.goalState.collectAsState(initial = GoalState.Loading)

    var slope = 0.0
    var intercept = 0.0
    var trendValues: List<Double> = emptyList()

    if (weights.count() > 2) {
        val result = TrendAnalysis.calculateLinearRegression(weights)
        slope = result.slope
        intercept = result.intercept
        trendValues = result.trendValues
    }

    // Scaffold with floating action button
    Scaffold(
        // Setup the snackbar host
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // Add a floating action button for adding new weights
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showWeightSheet = true },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Display the goal state
            when (val state = goalState) {
                // Loading state
                GoalState.Loading -> CircularProgressIndicator()

                // Goal not set message
                GoalState.NotSet -> GoalText(
                    "Your Goal HAS NOT BEEN SET!",
                    Color.Red,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                // Goal set, display the weight
                is GoalState.Set -> {
                    GoalText(
                        "Your Goal Weight Is: ${state.value} lbs",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    if (weights.count() > 2) {
                        val estimatedX = ((state.value - intercept) / slope)
                        val estimatedDate = Instant.ofEpochSecond(estimatedX.toLong())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        GoalText(
                            "Estimated Date To Reach Goal: ${estimatedDate.format(formatter)}",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display the weight change and weight to goal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Weight change card
                WeightChangeCard(
                    weightChange = weightChange,
                    modifier = Modifier.weight(1f)
                )

                // Weight to goal card
                WeightToGoalCard(
                    weightToGoal = weightToGoal,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            // Only show chart if there is data
            if (weights.count() > 2) {
                // Header for the recorded weights list
                Text(
                    text = "Recorded Weights",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                WeightLineChart(
                    weights = weights,
                    trendValues = trendValues,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                Text("No data yet", modifier = Modifier.padding(16.dp))
            }

            WeightInputBottomSheet(
                isVisible = showWeightSheet,
                onDismiss = { showWeightSheet = false },
                onSaveWeight = onSaveWeight,
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