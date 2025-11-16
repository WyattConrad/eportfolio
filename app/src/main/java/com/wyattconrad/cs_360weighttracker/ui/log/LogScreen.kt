package com.wyattconrad.cs_360weighttracker.ui.log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlinx.datetime.*
import kotlinx.datetime.format.*

@Composable
fun LogScreen(
    viewModel: LogViewModel = hiltViewModel(),
) {
    // Collect the Flow<List<Weight>> as state
    val weights by viewModel.weights.collectAsState(initial = emptyList())

    // State to control dialog visibility
    var showDialog by remember { mutableStateOf(false) }
    // State to store user input
    var inputWeight by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                content = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Weight")
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Weight Log",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = weights,
                    key = { it.id } // Make sure your Weight has an 'id'
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

        // Add Weight Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Add Weight") },
                text = {
                    TextField(
                        value = inputWeight,
                        onValueChange = { inputWeight = it },
                        label = { Text("Weight (kg)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val weightValue = inputWeight.toDoubleOrNull()
                            if (weightValue != null) {
                                viewModel.addWeight(weightValue) // Call a function in your ViewModel
                                inputWeight = ""
                                showDialog = false
                            } else {
                                // Optionally, show an error if input is invalid
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun WeightItem(
    weight: Weight,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            // Weight and Date section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${weight.weight} lbs",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = weight.dateTimeLogged.toKotlinLocalDateTime().format(LocalDateTime.Format {
                        monthNumber(); char('/'); dayOfMonth();
                        char('/'); year();
                        char(' ');
                        amPmHour(); char(':'); minute();
                        amPmMarker("AM", "PM")
                    }),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Spacer to push icons to the right
            Spacer(modifier = Modifier.width(16.dp))

            // Icon buttons for actions
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Weight",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Weight",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun EditWeightDialog(
    weight: Weight,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // State to hold the text field value, initialized with the current weight
    var text by remember { mutableStateOf(weight.weight.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Weight") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("New weight") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(text)
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun WeightItemPreview() {
    // 1. Create a fake Weight object for the preview.
    // Make sure the property names match your Weight model (e.g., .weight, .dateTimeLogged).
    val sampleWeight = Weight(
        185.5,
        1L
    )

    // 2. Call your WeightItem with the sample data and empty lambdas for the clicks.
    WeightItem(
        weight = sampleWeight,
        onEditClick = { /* Clicks do nothing in preview */ },
        onDeleteClick = { /* Clicks do nothing in preview */ },
        modifier = Modifier.padding(16.dp) // Add padding to see it clearly
    )
}