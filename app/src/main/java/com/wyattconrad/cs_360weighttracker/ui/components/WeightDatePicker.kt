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

import android.widget.DatePicker
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.wyattconrad.cs_360weighttracker.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun WeightDatePicker(
    selectedDateTime: Instant,
    onDateSelected: (Instant) -> Unit
) {
    val context = LocalContext.current
    val currentMillis = selectedDateTime.toEpochMilli()

    // Convert Instant to Calendar components for the Dialog
    val calendar = Calendar.getInstance().apply { timeInMillis = currentMillis }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    Button(
        onClick = {
        // Launch the DatePickerDialog
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                onDateSelected(Instant.ofEpochMilli(calendar.timeInMillis))
            }, year, month, day
        )
        datePickerDialog.show()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3B76F6)),
        ) {
        Text(
            // Format the date for display
            text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(currentMillis))
        )
    }
}

// Preview for the WeightDatePicker composable
@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    AppTheme {
        var selectedDateTime by remember {
            mutableStateOf(Instant.ofEpochMilli(System.currentTimeMillis()))
        }
        WeightDatePicker(
            selectedDateTime = selectedDateTime,
            onDateSelected = { newDate ->
                selectedDateTime = newDate
            }
        )
    }
}