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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wyattconrad.cs_360weighttracker.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for users.
 * @author Wyatt Conrad
 * @version 1.0
 */
@Dao
interface UserDao {

    // Get a user by their username case insensitive
    @Query("SELECT * FROM users WHERE LOWER(username) = LOWER(:username) LIMIT 1")
    fun getUserByUsername(username: String?): User?

    // Get a user by username or email case insensitive
    @Query("""
    SELECT * FROM users 
    WHERE LOWER(username) = LOWER(:input)
    OR LOWER(email) = LOWER(:input)
    LIMIT 1
""")
    suspend fun getUserByUsernameOrEmail(input: String): User?

    // Get all users
    @get:Query("SELECT * FROM users")
    val allUsers: Flow<MutableList<User?>?>

    // Get the count of users by username
    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun countUsersByUsername(username: String?): Int

    // Check if a user exists by their username
    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE username = :username)")
    fun userExists(username: String?): Flow<Boolean>

    // Get the id of a user by their username
    @Query("SELECT id FROM users WHERE username = :username")
    fun getUserId(username: String?): Flow<Long?>

    // Get the first name of a user by their id
    @Query("SELECT first_name FROM users WHERE id = :userId")
    fun getUserFirstName(userId: Long): Flow<String?>

    // Suspend is used for the following functions to run them on a separate thread
    // Insert a user into the database
    @Insert
    suspend fun insertUser(user: User): Long

    // Update a user in the database
    @Update
    suspend fun updateUser(user: User)

    // Delete a user from the database
    @Delete
    suspend fun deleteUser(user: User)

    // Delete all users from the database
    @Query("DELETE FROM users")
    suspend fun deleteAll()

    // Get a user by their id
    @Query("SELECT * FROM users WHERE id = :userId")
    fun fetchUser(userId: Long): Flow<User?>

    // Get the username of a user by their id
    @Query("SELECT username FROM users WHERE id = :userId")
    fun getUsername(userId: Long): Flow<String?>
}
