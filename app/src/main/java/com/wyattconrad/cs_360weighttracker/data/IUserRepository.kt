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
package com.wyattconrad.cs_360weighttracker.data

import com.wyattconrad.cs_360weighttracker.data.UserRepository.UsernameCallback
import com.wyattconrad.cs_360weighttracker.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Sets up the interface for the UserRepository.
 */
interface IUserRepository {

    // Logs a user into the application.
    fun login(username: String?, password: String?): Flow<User?>

    // Get the User ID for a given username.
    fun getUserId(username: String?): Flow<Long?>

    // Get the first name for a given user ID.
    fun getUserFirstName(userId: Long): Flow<String?>

    // Check if the user exists in the database
    fun userExists(username: String?): Flow<Boolean?>

    // Get the user record for a given user ID.
    fun fetchUser(userId: Long): Flow<User?>

    // Get the username for a user for a given user ID.
    fun getUsername(userId: Long): Flow<String?>

    // Register a new User
    suspend fun registerUser(user: User)

    // Check if the username already exists in the database, including a callback function
    fun checkForExistingUsername(username: String?, callback: UsernameCallback)

    // Check if the username already exists in the database
    fun checkForExistingUsername(username: String) : Boolean

    // Delete all users from the database
    suspend fun deleteAll()
}