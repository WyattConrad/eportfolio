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
package com.wyattconrad.cs_360weighttracker.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wyattconrad.cs_360weighttracker.Home
import com.wyattconrad.cs_360weighttracker.R
import com.wyattconrad.cs_360weighttracker.Register

/**
 * Composable function for the registration screen.
 * @param onBackToLoginClick Callback to be invoked when the back to login link is clicked.
 * @param showToast Callback to be invoked to show a toast message.
 * @param viewModel ViewModel for the registration screen.
 * @param navController NavController for navigation.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */


@Composable
fun RegistrationScreen(
    onBackToLoginClick: () -> Unit,
    showToast: (String) -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel(),
    navController: NavController,
) {
    // Collect State from ViewModel
    val state by viewModel.uiState.collectAsState()

    // Collect One-Time Events from ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegistrationEvent.RegistrationSuccess -> {
                    // Navigate to Home screen after successful registration
                    navController.navigate(Home.route) {
                        popUpTo(Register.route) { inclusive = true }
                    }
                }
                is RegistrationEvent.RegistrationFailure -> {
                    showToast(event.message)
                }
                is RegistrationEvent.NavigateToLogin -> onBackToLoginClick()
            }
        }
    }

    // Enable register button only if:
    // 1. All fields are non-empty.
    // 2. Passwords match.
    // 3. There are no active username errors.
    // 4. The app is not currently registering.
    val isRegisterEnabled = state.firstName.isNotBlank() &&
            state.email.isNotBlank() &&
            state.username.isNotBlank() &&
            state.password.isNotBlank() &&
            state.password == state.confirmPassword &&
            state.usernameError == null &&
            !state.isRegistering

    // Column layout for the registration screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header Text
        Text(
            text = stringResource(R.string.register_your_account),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // First Name
        TextField(
            value = state.firstName,
            onValueChange = { viewModel.updateField("firstName", it) },
            placeholder = { Text(stringResource(R.string.first_name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Last Name
        TextField(
            value = state.lastName,
            onValueChange = { viewModel.updateField("lastName", it) },
            placeholder = { Text(stringResource(R.string.last_name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        TextField(
            value = state.email,
            onValueChange = { viewModel.updateField("email", it) },
            placeholder = { Text(stringResource(R.string.email)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Username Input
        TextField(
            value = state.username,
            onValueChange = { viewModel.updateField("username", it) },
            placeholder = { Text(stringResource(R.string.username)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .onFocusChanged { focusState ->
                    // This replaces the Fragment's onFocusChangeListener for username check
                    if (!focusState.isFocused && state.username.isNotBlank()) {
                        viewModel.checkUsernameExists(state.username)
                    }
                },
            isError = state.usernameError != null,
            // Error message below the field
            supportingText = {
                if (state.usernameError != null) {
                    Text(text = state.usernameError!!, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        TextField(
            value = state.password,
            onValueChange = { viewModel.updateField("password", it) },
            placeholder = { Text(stringResource(R.string.password)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation(),
            isError = state.passwordError != null,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password Input
        TextField(
            value = state.confirmPassword,
            onValueChange = { viewModel.updateField("confirmPassword", it) },
            placeholder = { Text(stringResource(R.string.password)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = PasswordVisualTransformation(),
            isError = state.passwordError != null,
            // Error message below the confirm password field
            supportingText = {
                if (state.passwordError != null) {
                    Text(text = state.passwordError!!, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Register Button
        Button(
            onClick = viewModel::registerUser,
            modifier = Modifier.width(150.dp),
            enabled = isRegisterEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A86B)) // green_blue
        ) {
            // Show a progress indicator while registering
            if (state.isRegistering) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text(text = stringResource(R.string.submit))
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Back to Login Link
        TextButton(onClick = onBackToLoginClick) {
            Text(
                text = stringResource(R.string.back_to_login_page),
                fontSize = 18.sp,
                color = Color(0xFF3B76F6) // French Blue
            )
        }
    }
}
