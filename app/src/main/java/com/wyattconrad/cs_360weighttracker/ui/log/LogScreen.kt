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
package com.wyattconrad.cs_360weighttracker.ui.log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.ui.components.EditWeight
import com.wyattconrad.cs_360weighttracker.ui.components.WeightLogList
import kotlinx.coroutines.launch

/**
 * LogScreen Composable
 *
 * This Composable represents the main screen of the application for logging weights.
 * @param viewModel The ViewModel associated with this screen.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun LogScreen(
    viewModel: LogViewModel = hiltViewModel(),
) {
    // Collect the Flow<List<Weight>> as state
    val weights by viewModel.weights.collectAsState(initial = emptyList())

    // State to track the weight being edited
    var weightToEdit by remember { mutableStateOf<Weight?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    // State to track the weight being deleted
    var weightToDelete by remember { mutableStateOf<Weight?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // State for snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // State to control dialog visibility
    var showDialog by remember { mutableStateOf(false) }
    // State to store user input
    var inputWeight by remember { mutableStateOf("") }

    // Scaffold with floating action button
    Scaffold(
        // Setup the snackbar host
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // Add a floating action button for adding new weights
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF4CAF50),
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Weight")
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        // Column to hold the weight log list
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Weight Log List
            WeightLogList(
                weights = weights.reversed(),
                onEditClick = { weight ->
                    weightToEdit = weight
                    showEditDialog = true
                },
                onDeleteClick = { weight ->
                    weightToDelete = weight
                    showDeleteDialog = true
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // Add Weight Dialog
        if (showDialog) {
            // AlertDialog for adding a new weight
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Add Weight") },
                text = {
                    // Text field for entering weight
                    TextField(
                        value = inputWeight,
                        onValueChange = { inputWeight = it },
                        label = { Text("Weight (kg)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    // Button to confirm weight addition
                    TextButton(
                        onClick = {
                            val weightValue = inputWeight.toDoubleOrNull()
                            if (weightValue != null) {
                                viewModel.addWeight(weightValue)
                                inputWeight = ""
                                showDialog = false
                            } else {
                                // If weight value is null, show error message

                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    // Cancel button
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Handle the edit dialog
        if (showEditDialog && weightToEdit != null) {
            EditWeight(
                weight = weightToEdit!!,
                onDismiss = {
                    showEditDialog = false
                    weightToEdit = null
                },
                onConfirm = { newValue ->
                    val newWeight = newValue.toDoubleOrNull()
                    if (newWeight != null) {
                        viewModel.updateWeight(weightToEdit!!, newWeight)
                    }
                    showEditDialog = false
                    weightToEdit = null
                }
            )
        }

        // Handle the delete dialog
        if (showDeleteDialog && weightToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                    weightToDelete = null
                },
                title = { Text("Delete Weight") },
                text = {
                    Text("Are you sure you want to delete this weight entry?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteWeight(weightToDelete!!)
                            showDeleteDialog = false

                            // SHOW SNACKBAR
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Weight deleted",
                                    duration = SnackbarDuration.Short
                                )
                            }

                            weightToDelete = null
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            weightToDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}





