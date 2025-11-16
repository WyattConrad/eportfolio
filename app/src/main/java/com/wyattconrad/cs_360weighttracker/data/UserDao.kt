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

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String?): Flow<User?>

    @get:Query("SELECT * FROM users")
    val allUsers: Flow<MutableList<User?>?>

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    fun countUsersByUsername(username: String?): Int

    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE username = :username)")
    fun userExists(username: String?): Flow<Boolean?>

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    fun login(username: String?, password: String?): Flow<User?>

    @Query("SELECT id FROM users WHERE username = :username")
    fun getUserId(username: String?): Flow<Long?>

    @Query("SELECT first_name FROM users WHERE id = :userId")
    fun getUserFirstName(userId: Long): Flow<String?>

    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAll()


    @Query("SELECT * FROM users WHERE id = :userId")
    fun fetchUser(userId: Long): Flow<User?>

    @Query("SELECT username FROM users WHERE id = :userId")
    fun getUsername(userId: Long): Flow<String?>
}
