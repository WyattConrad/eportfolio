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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.wyattconrad.cs_360weighttracker.ui.components.WeightItem
import com.wyattconrad.cs_360weighttracker.ui.log.LogEvent

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {

    // Collect the Flow<List<Weight>> as state
    val weights by viewModel.weights.collectAsState(initial = emptyList())
    val weightChange by viewModel.weightChange.collectAsState(initial = 0.0)
    val weightToGoal by viewModel.weightToGoal.collectAsState(initial = 0.0)
    val goalState by viewModel.goalState.collectAsState(initial = GoalState.Loading)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val state = goalState) {
            GoalState.Loading -> CircularProgressIndicator()
            GoalState.NotSet -> GoalText(
                "Your Goal HAS NOT BEEN SET!",
                Color.Red,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            is GoalState.Set -> GoalText(
                "Your Goal Weight Is: ${state.value} lbs",
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Weight Lost:", color = Color.White)
                    Text(weightChange.toString(), fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("lbs.", color = Color.White)
                }
            }

            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Left To Goal:", color = Color.White)
                    Text(weightToGoal.toString(), fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("lbs.", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(64.dp))

        Text(
            text = "Recorded Weights",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = weights,
                key = { it.id }
            ) { weight ->
                WeightItem(
                    weight = weight,
                    onEditClick = { LogEvent.EditWeight(weight) },
                    onDeleteClick = { LogEvent.DeleteWeight(weight) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}


@Composable
fun GoalText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface, // default
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    maxFontSize: TextUnit = 24.sp,  // largest font size
    minFontSize: TextUnit = 14.sp   // smallest font size
){
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        // Adjust font size based on maxWidth
        val fontSize = (maxWidth.value / 20).coerceIn(minFontSize.value, maxFontSize.value).sp

        Text(
            text = text,
            fontSize = fontSize,
            color = color,
            style = style,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}