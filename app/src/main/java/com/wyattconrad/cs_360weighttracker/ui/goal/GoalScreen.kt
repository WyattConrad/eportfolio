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
package com.wyattconrad.cs_360weighttracker.ui.goal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.wyattconrad.cs_360weighttracker.R
import com.wyattconrad.cs_360weighttracker.ui.log.LogViewModel

/**
 * A composable function representing the goal screen of the weight tracker app.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun GoalScreen(
    viewModel: GoalViewModel = hiltViewModel(),
) {
    val goal by viewModel.currentGoal.collectAsState(initial = null)

    var goalValue by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    // When the DB goal changes AND we are not editing, update local text
    LaunchedEffect(goal, isEditing) {
        if (!isEditing) {
            goalValue = goal?.goal?.toString() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.baseline_monitor_weight_24),
            contentDescription = stringResource(R.string.scale_image),
            colorFilter = ColorFilter.tint(Color(0xFF0075C4)),
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.your_current_goal),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = goalValue,
            onValueChange = { goalValue = it },
            placeholder = { Text(text = stringResource(R.string.weight_in_lbs)) },
            singleLine = true,
            enabled = isEditing,
            modifier = Modifier.wrapContentWidth(),
            textStyle = TextStyle(
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditing) {
            Button(
                onClick = {
                    val weightVal = goalValue.toDoubleOrNull()
                    if (weightVal != null) {
                        viewModel.saveGoal(weightVal)
                    }
                    isEditing = false
                },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(text = stringResource(R.string.save), fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { isEditing = true },
            modifier = Modifier.wrapContentWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text(
                text = stringResource(R.string.update_goal),
                fontSize = 18.sp,
                color = Color(0xFF3B76F6)
            )
        }
    }
}

// Preview for the goal screen
@Preview(showBackground = true)
@Composable
fun GoalScreenPreview() {
    // Optional: Wrap in MaterialTheme for proper styling
    androidx.compose.material3.MaterialTheme {
        GoalScreen()
    }
}