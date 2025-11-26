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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Composable function for displaying the weight summary section.
 *
 * @param weightChange The weight change value.
 * @param weightToGoal The weight to goal value.
 * @param modifier The modifier for the composable.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun WeightSummarySection(
    weightChange: Double,
    weightToGoal: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        WeightChangeCard(
            weightChange = weightChange,
            modifier = Modifier.weight(1f)
        )

        WeightToGoalCard(
            weightToGoal = weightToGoal,
            modifier = Modifier.weight(1f)
        )
    }
}

// Preview for the WeightSummarySection composable
@Preview(showBackground = true)
@Composable
fun WeightSummarySectionPreview() {
    WeightSummarySection(
        weightChange = 10.0,
        weightToGoal = 5.0
    )
}
