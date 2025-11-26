package com.wyattconrad.cs_360weighttracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wyattconrad.cs_360weighttracker.ui.theme.AppTheme
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightInputBottomSheet(
    isVisible: Boolean,
    inputWeight: Double,
    selectedDateTime: Instant,
    onDismiss: () -> Unit,
    onSaveWeight: (Double, Instant) -> Unit
) {

    var weightText by remember(inputWeight) {
        mutableStateOf(if (inputWeight > 0) inputWeight.toString() else "")
    }
    var currentDateTime by remember(selectedDateTime) { mutableStateOf(selectedDateTime) }

    if (isVisible) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Enter Weight",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(16.dp))


                // Text field for entering weight
                TextField(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Weight (lbs)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Date/Time Recorded",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                // Date and Time Pickers in a Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeightDatePicker(
                        selectedDateTime = currentDateTime,
                        onDateSelected = { newDate ->
                            currentDateTime = newDate
                        }
                    )
                    WeightTimePicker(
                        selectedDateTime = currentDateTime,
                        onTimeSelected = { newTime ->
                            currentDateTime = newTime
                        }
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = {
                        // Pass BOTH values up to the ViewModel
                        val weightDouble = weightText.toDoubleOrNull() ?: 0.0
                        onSaveWeight(weightDouble, currentDateTime)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B76F6)),
                    ) {
                    Text("Save Weight")
                }
            }
        }
    }
}

// Preview for the WeightInputBottomSheet composable
@Preview(showBackground = true)
@Composable
fun WeightInputBottomSheetPreview() {
    AppTheme {
        var showSheet by remember { mutableStateOf(true) }

        Column(modifier = Modifier.padding(16.dp)) {
            Button(onClick = { showSheet = true }) {
                Text("Open Weight Input")
            }
        }

        WeightInputBottomSheet(
            isVisible = showSheet,
            inputWeight = 150.0,
            selectedDateTime = Instant.now(),
            onDismiss = { showSheet = false },
            onSaveWeight = { weight, time ->
                showSheet = false
            }
        )
    }
}
