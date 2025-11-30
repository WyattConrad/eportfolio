package com.wyattconrad.cs_360weighttracker.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onPhoneComplete: (String) -> Unit,
    events: Flow<SettingsEvent>,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isFocused by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Collect events
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                SettingsEvent.SmsFailed -> {
                    Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_LONG).show()
                }
                SettingsEvent.SmsSent -> {
                    Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Customize Your Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // SECTION: SMS Notifications
            SectionHeader(title = "SMS Notifications")

            SettingItem(
                title = "SMS Notifications",
                description = "Enable/Disable SMS Notifications",
                checked = uiState.smsEnabled,
                onCheckedChange = { viewModel.toggleSms(it) }
            )

            // Phone Number Field If SMS enabled
            if (uiState.showPhoneInput) {
                OutlinedTextField(
                    value = uiState.phoneNumber,
                    minLines = 1,
                    maxLines = 1,
                    onValueChange = { viewModel.onPhoneNumberChange(it) },
                    label = { Text("Phone Number") },
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (isFocused && !focusState.isFocused) {
                            // Focus was lost → field was completed
                            onPhoneComplete(uiState.phoneNumber)
                        }
                        isFocused = focusState.isFocused
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onPhoneComplete(uiState.phoneNumber)
                        }
                    )
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            // SECTION: In-app
            SectionHeader(title = "In-App Notifications")

            SettingItem(
                title = "In-App Notifications",
                description = "Enable/Disable In-App Notifications",
                checked = uiState.inAppEnabled,
                onCheckedChange = { viewModel.toggleInApp(it) }
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Medium)
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF0075C4),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray)
        )
    }
}
