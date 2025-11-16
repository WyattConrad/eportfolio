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

interface IUserRepository {
    fun login(username: String?, password: String?): Flow<User?>
    fun getUserId(username: String?): Flow<Long?>
    fun getUserFirstName(userId: Long): Flow<String?>
    fun userExists(username: String?): Flow<Boolean?>
    fun fetchUser(userId: Long): Flow<User?>
    fun getUsername(userId: Long): Flow<String?>
    suspend fun registerUser(user: User)
    fun checkForExistingUsername(username: String?, callback: UsernameCallback)
    fun checkForExistingUsername(username: String) : Boolean
    suspend fun deleteAll()
}