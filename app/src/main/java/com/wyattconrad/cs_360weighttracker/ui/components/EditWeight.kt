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

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.wyattconrad.cs_360weighttracker.model.Weight

/**
 * Composable function for editing a weight entry.
 *
 * @param weight The weight entry to be edited.
 * @param onDismiss Callback to be invoked when the dialog is dismissed.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun EditWeight(
    weight: Weight,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // State to hold the text field value, initialized with the current weight
    var text by remember { mutableStateOf(weight.weight.toString()) }

    // AlertDialog composable to display the dialog
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Weight") },
        text = {
            // OutlinedTextField for entering a new weight
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("New weight") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            // Button to confirm the update
            TextButton(
                onClick = {
                    onConfirm(text)
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            // Button to cancel the update
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}