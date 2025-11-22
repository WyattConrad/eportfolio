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
package com.wyattconrad.cs_360weighttracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A ViewModel for the Login Screen
 * @param prefs The UserPreferencesService
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val prefs: UserPreferencesService
) : ViewModel() {

    // Variables to hold the first name and logged in state of the user
    private val _loggedInUserId = MutableStateFlow(-1L)
    val loggedInUserId: StateFlow<Long> = _loggedInUserId.asStateFlow()

    // StateFlow for user first name
    private val _userFirstName = MutableStateFlow("Guest")
    val userFirstName: StateFlow<String> = _userFirstName.asStateFlow()

    // Initialize the session
    init {
        loadSession()
    }

    // Load the session
    fun loadSession() {
        val id = prefs.getGlobalLong("userId")
        _loggedInUserId.value = id
        if (id != -1L) {
            viewModelScope.launch {
                val firstName = prefs.getString(id, "first_name", "Guest") ?: "Guest"
                _userFirstName.value = firstName
            }
        } else {
            _userFirstName.value = "Guest"
        }
    }

    // Call this when user logs in
    fun setUserSession(userId: Long, firstName: String) {
        _loggedInUserId.value = userId
        _userFirstName.value = firstName
    }

    // Call this when logging out
    fun clearSession() {
        _loggedInUserId.value = -1L
        _userFirstName.value = "Guest"
    }
}
