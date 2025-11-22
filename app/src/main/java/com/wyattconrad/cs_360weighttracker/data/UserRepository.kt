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
import com.wyattconrad.cs_360weighttracker.service.HashingService
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the user repository interface.
 * @author Wyatt Conrad
 * @version 1.0
 */
class UserRepository(
    private val userDao: UserDao,
    private val hashService: HashingService

) : IUserRepository {

    //Login method
    override suspend fun login(username: String, password: String): LoginResult {
        val user = userDao.getUserByUsernameOrEmail(username) ?: return LoginResult.UserNotFound

        val verified = hashService.verifyPassword(password, user.hashedPassword)

        if (!verified) {
            return LoginResult.InvalidCredentials
        }

        return LoginResult.Success(user.id, user.firstName)
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
    override fun userExists(username: String?): Flow<Boolean> {
        return userDao.userExists(username)
    }

    // Fetch the user
    override fun fetchUser(userId: Long): Flow<User?> {
        return userDao.fetchUser(userId)
    }

    // Get the username of a user
    override fun getUsername(userId: Long): Flow<String?> {
        return userDao.getUsername(userId)
    }

    // Suspend is used for the following functions to run them on a separate thread
    // Register New User
    override suspend fun registerUser(user: User) {

        // Hash the password before saving it
        val hashedPassword = hashService.hashPassword(user.hashedPassword)

        // Update the user with the hashed password
        val userToRegister = user.copy(hashedPassword = hashedPassword)

        // Insert the user into the database
        val userId = userDao.insertUser(userToRegister)
        user.id = userId
    }


    // Create an interface for the callback
    fun interface UsernameCallback {
        fun onUsernameExists(exists: Boolean)
    }

    // Check if username already exists
    override suspend fun checkForExistingUsername(username: String?, callback: UsernameCallback) {
            // Check if the username already exists in the database
            val count = userDao.countUsersByUsername(username)
            callback.onUsernameExists(count > 0)
    }

    // Check if username already exists
    override suspend fun checkForExistingUsername(username: String): Boolean {
            return userDao.countUsersByUsername(username) > 0
    }

    // Delete all users
    override suspend fun deleteAll() {
        userDao.deleteAll()
    }
}
