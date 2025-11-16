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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.model.User
import javax.inject.Inject

/**
 * A ViewModel for the Login Screen
 * @param userRepository The repository for the User data
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Get the text for the login button
    fun getText() : LiveData<String> {
        return MutableLiveData<String>("Please Login")
    }

    // Login a user
    fun login(username: String?, password: String?): LiveData<User?> {
        return userRepository.login(username, password).asLiveData()
    }

    // Get the user id for a username
    fun getUserId(username: String?): LiveData<Long?> {
        return userRepository.getUserId(username).asLiveData()
    }
}