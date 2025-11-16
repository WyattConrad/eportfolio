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


import com.wyattconrad.cs_360weighttracker.model.User
import kotlinx.coroutines.flow.Flow


class UserRepository(
    private val userDao: UserDao,

) : IUserRepository {

    //Login method
    override fun login(username: String?, password: String?): Flow<User?> {
        return userDao.login(username, password)
    }

    // Get User Id
    override fun getUserId(username: String?): Flow<Long?> {
        return userDao.getUserId(username)
    }

    // Get User First Name
    override fun getUserFirstName(userId: Long): Flow<String?> {
        return userDao.getUserFirstName(userId)
    }

    // Check if user exists
    override fun userExists(username: String?): Flow<Boolean?> {
        return userDao.userExists(username)
    }

    // Fetch the user
    override fun fetchUser(userId: Long): Flow<User?> {
        return userDao.fetchUser(userId)
    }

    override fun getUsername(userId: Long): Flow<String?> {
        return userDao.getUsername(userId)
    }

    // Register New User
    override suspend fun registerUser(user: User) {
            val userId = userDao.insertUser(user)
            user.id = userId
    }


    // Create an interface for the callback
    interface UsernameCallback {
        fun onUsernameExists(exists: Boolean)
    }

    // Check if username already exists
    override fun checkForExistingUsername(username: String?, callback: UsernameCallback) {
            // Check if the username already exists in the database
            val count = userDao.countUsersByUsername(username)
            callback.onUsernameExists(count > 0)
    }

    // Check if username already exists
    override fun checkForExistingUsername(username: String): Boolean {
            return if(userDao.countUsersByUsername(username) > 0) true else false
    }

    override suspend fun deleteAll() {
        userDao.deleteAll()
    }
}
