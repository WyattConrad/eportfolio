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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.LoginResult
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.model.User
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for the registration screen.
 * @property userRepository The repository for the user data.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val prefs: UserPreferencesService,
) : ViewModel() {

    // Mutable state for the UI, exposed as StateFlow
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    // Channel for one-time events, like navigation or toast messages
    private val _events = Channel<RegistrationEvent>()
    val events = _events.receiveAsFlow()

    // Function to update any field in the UI state
    fun updateField(fieldName: String, value: String) {
        _uiState.update { currentState ->
            when (fieldName) {
                "firstName" -> currentState.copy(firstName = value)
                "lastName" -> currentState.copy(lastName = value)
                "email" -> currentState.copy(email = value)
                "username" -> currentState.copy(username = value, usernameError = null) // Clear error on change
                "password" -> currentState.copy(password = value, passwordError = null) // Clear error on change
                "confirmPassword" -> currentState.copy(confirmPassword = value, passwordError = null) // Clear error on change
                else -> currentState
            }
        }
    }

    // Coroutine to check for existing username
    fun checkUsernameExists(username: String) {
        // Only check if username is not blank
        if (username.isBlank()) return

        viewModelScope.launch {

            userRepository.checkForExistingUsername(username) { usernameExists ->
                if (usernameExists) {
                    _uiState.update { it.copy(usernameError = "Username already exists") }
                } else {
                    _uiState.update { it.copy(usernameError = null) }
                }
            }
        }
    }

    // Main registration logic, replacing the Fragment's `register()` method
    fun registerUser() {
        val state = _uiState.value

        // **Input Validation**
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(passwordError = "Passwords do not match") }
            return
        }

        // Check if username has an active error
        if (state.usernameError != null) {
            return
        }

        // Check for other blank fields (though the button should be disabled)
        if (state.firstName.isBlank() || state.lastName.isBlank() || state.username.isBlank() || state.password.isBlank()) {
            // This should not happen if isRegisterEnabled works, but good for safety
            return
        }

        // Start registration process
        _uiState.update { it.copy(isRegistering = true, passwordError = null) }

        viewModelScope.launch {
            try {
                // Create User and save it to the database
                val newUser = User(state.firstName, state.lastName, state.email, state.username, state.password)
                userRepository.registerUser(newUser)

                // Login User to get ID
                val loginResult = userRepository.login(state.username, state.password)
                val userId = (loginResult as? LoginResult.Success)?.userId

                if (userId != null && userId != -1L) {

                    // Save Preferences and Login State
                    prefs.putGlobalLong("userId", userId)
                    prefs.putString(userId, "first_name", state.firstName)

                    // Send Success Event to the screen
                    _events.send(RegistrationEvent.RegistrationSuccess)
                } else {
                    // This handles login failure after a successful registration (if possible)
                    _uiState.update { it.copy(passwordError = "Login failed after registration.") }
                    _events.send(RegistrationEvent.RegistrationFailure("Login failed after registration."))
                }

            } catch (e: Exception) {
                // General error handling (e.g., network error, registration failure)
                _events.send(RegistrationEvent.RegistrationFailure("Registration failed: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isRegistering = false) }
            }
        }
    }
}

// Data class to hold the UI state
data class RegistrationUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isRegistering: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null
)

// Sealed class for one-time events (Navigation, Toast)
sealed class RegistrationEvent {
    data object RegistrationSuccess : RegistrationEvent()
    data class RegistrationFailure(val message: String) : RegistrationEvent()
    data object NavigateToLogin : RegistrationEvent()
}