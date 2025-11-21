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
package com.wyattconrad.cs_360weighttracker.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.LoginResult
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A ViewModel for the Login Screen
 * @param userRepository The repository for the User data
 * @param prefs The shared preferences service
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val prefs: UserPreferencesService
) : ViewModel() {

    // Establish a UI State
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    // Update the email/username field as it is changed
    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    // Update the password field as it is changed
    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    // Process the sign in button click event
    fun login(username: String, password: String) {
        viewModelScope.launch {

            // Update the button to show a spinner
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Call teh database with the username and password
                when (val result = userRepository.login(username, password)) {
                    // if username and password match
                    is LoginResult.Success -> {

                        // Save the user id and first name to shared preferences
                        prefs.putGlobalLong("userId", result.userId)
                        prefs.putString(result.userId, "first_name", result.firstName)

                        // Update the UI state to show that the user is logged in
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                errorMessage = null
                            )
                        }
                    }

                    is LoginResult.InvalidCredentials -> {
                        // Make sure the user id is set to -1
                        prefs.putGlobalLong("userId", -1L)

                        // Update the UI state to show that the user is not logged in
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = false,
                                errorMessage = "Invalid password."
                            )
                        }

                    }
                    // if username and password do not match
                    is LoginResult.Error -> {

                        // Make sure the user id is set to -1
                        prefs.putGlobalLong("userId", -1L)

                        // Update the UI state to show that the user is not logged in
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = false,
                                errorMessage = result.message
                            )
                        }
                    }

                    // if something else goes wrong (user doesn't exist, etc.)
                    else -> {
                        Log.i("LOGIN", "Unknown result (null?)")
                        prefs.putGlobalLong("userId", -1L)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Something went wrong...",
                                isLoggedIn = false
                            )
                        }
                    }
                }
                // Catch any exceptions and update the UI state accordingly
            } catch (e: Exception) {
                Log.e("LOGIN", "Exception during login", e)
                prefs.putGlobalLong("userId", -1L)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unexpected error: ${e.message}",
                        isLoggedIn = false
                    )
                }
            }
        }
    }

}

/**
 * A data class for the Login UI State
 */
data class LoginUiState(
    val userId: Long = -1L,
    val firstName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)