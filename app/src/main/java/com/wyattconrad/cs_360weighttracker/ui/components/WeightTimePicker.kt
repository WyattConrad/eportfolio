package com.wyattconrad.cs_360weighttracker.ui.components

import android.app.TimePickerDialog
import android.widget.TimePicker
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
fun WeightTimePicker(
    selectedDateTime: Instant,
    onTimeSelected: (Instant) -> Unit
) {
    val context = LocalContext.current
    val currentMillis = selectedDateTime.toEpochMilli()

    // Convert Instant to Calendar components for the Dialog
    val calendar = Calendar.getInstance().apply { timeInMillis = currentMillis }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    Button(
        onClick = {
        // Launch the TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                onTimeSelected(Instant.ofEpochMilli(calendar.timeInMillis))
            }, hour, minute, false
        )
        timePickerDialog.show()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3B76F6)),
        ) {
        Text(
            // Format the time for display
            text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(currentMillis))
        )
    }
}

// Preview for the WeightTimePicker composable
@Preview(showBackground = true)
@Composable
fun WeightTimePickerPreview() {
    AppTheme {
        var selectedDateTime by remember {
            mutableStateOf(Instant.ofEpochMilli(System.currentTimeMillis()))
        }
        WeightTimePicker(
            selectedDateTime = selectedDateTime,
            onTimeSelected = { newDate ->
                selectedDateTime = newDate
            }
        )
    }
}