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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wyattconrad.cs_360weighttracker.ui.home.GoalState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Composable function for displaying the goal section.
 *
 * @param goalState The goal state.
 * @param estimatedGoalDate The estimated goal date.
 * @param modifier The modifier for the composable.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun GoalSection(
    goalState: GoalState,
    estimatedGoalDate: LocalDate?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (goalState) {
            GoalState.Loading -> {
                CircularProgressIndicator()
            }

            GoalState.NotSet -> {
                GoalText(
                    modifier = Modifier.padding(vertical = 16.dp),
                    text = "Your Goal HAS NOT BEEN SET!",
                    color = Color.Red
                )
            }

            is GoalState.Set -> {
                GoalText(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = "Your Goal Weight Is: ${goalState.value} lbs",
                )

                if (estimatedGoalDate != null) {
                    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                    GoalText(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = "Estimated Date To Reach Goal: ${estimatedGoalDate.format(formatter)}"
                    )
                }
            }
        }
    }
}

// Preview for the GoalSection composable
@Preview(showBackground = true)
@Composable
fun GoalSectionPreview(){
    GoalSection(
        goalState = GoalState.Set(135.0),
        estimatedGoalDate = LocalDate.now()
    )
}