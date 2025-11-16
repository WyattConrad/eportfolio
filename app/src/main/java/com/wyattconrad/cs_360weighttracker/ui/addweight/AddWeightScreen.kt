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
package com.wyattconrad.cs_360weighttracker.ui.addweight

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import com.wyattconrad.cs_360weighttracker.R

/**
 * Composable for adding weight to the log
 */
@Composable
fun AddWeightScreen() {

    // State variables for weight input and whether to show congrats message
    var weightInput by remember { mutableStateOf("") }
    var showCongrats by remember { mutableStateOf(false) }

    // Enable submit button only if weightInput is not blank and is a valid number
    val isSubmitEnabled = weightInput.toDoubleOrNull() != null && weightInput.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Weight Icon
        Icon(
            painter = painterResource(id = R.drawable.baseline_monitor_weight_24),
            contentDescription = stringResource(R.string.scale_image),
            tint = Color(0xFF3B76F6), // French Blue
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Header Text
        Text(
            text = stringResource(R.string.enter_your_current_weight),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Weight Entry Field with suffix
        OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            placeholder = { Text(stringResource(R.string.weight)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            trailingIcon = { Text("lbs.") },
            textStyle = LocalTextStyle.current.copy(fontSize = 22.sp),
            modifier = Modifier.width(150.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = { showCongrats = true },
            enabled = isSubmitEnabled,
            modifier = Modifier.width(150.dp)
        ) {
            Text(
                text = stringResource(R.string.submit),
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Congrats Text
        if (showCongrats) {
            Text(
                text = stringResource(R.string.congrats),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
