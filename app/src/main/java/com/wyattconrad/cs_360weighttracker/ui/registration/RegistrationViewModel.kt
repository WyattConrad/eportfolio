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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.data.UserRepository.UsernameCallback
import com.wyattconrad.cs_360weighttracker.model.User
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for the registration screen.
 * @property userRepository The repository for the user data.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Register new user
    suspend fun registerUser(user: User) {
        userRepository.registerUser(user)
    }

    // Register new user with coroutine
    fun registerUserCoroutine(user: User){
        viewModelScope.launch {
            registerUser(user)
        }
    }

    // Login user
    fun login(username: String?, password: String?): LiveData<User?> {
        return userRepository.login(username, password).asLiveData()
    }

    // Check if username already exists
    fun checkForExistingUsername(username: String?, callback: UsernameCallback) {
        userRepository.checkForExistingUsername(username, callback)
    }
}