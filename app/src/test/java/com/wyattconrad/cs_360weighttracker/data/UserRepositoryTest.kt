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

import android.content.SharedPreferences
import com.wyattconrad.cs_360weighttracker.model.User
import com.wyattconrad.cs_360weighttracker.service.HashingService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Tests for the UserRepository class.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
class UserRepositoryTest {
    // Setup mock dependencies
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository
    private lateinit var hashingService: HashingService

    private lateinit var sharedPreferences: SharedPreferences

    // Initialize the mocks
    @Before
    fun setup() {
        MockKAnnotations.init(this)

        userDao = mockk()
        sharedPreferences = mockk()
        hashingService = HashingService()


        userRepository = UserRepository(userDao, hashingService)

    }

    // Test the userExists method
    @Test
    fun userExists_for_a_non_existent_user() = runTest {
        val nonExistentUsername = "NonExistentUser"

        // Mock the DAO Flow
        coEvery { userDao.userExists(nonExistentUsername) } returns flowOf(false)

        // Call the repository method
        val resultFlow: Flow<Boolean> = userRepository.userExists(nonExistentUsername)

        // Collect the first value and assert
        val result = resultFlow.first()
        assertEquals(false, result)
    }

    // Test the registerUser method
    @Test
    fun register_a_new_unique_user() = runTest {
        // Create a new user
        val newUser = User(1L,"Mock", "User", "mock.user@email.com", "mockuser", "password")

        // Mock DAO insertUser to return an ID (e.g., 1L)
        coEvery { userDao.insertUser(newUser) } returns 1L

        // Call the repository method
        userRepository.registerUser(newUser)

        // Verify that insertUser was called
        coVerify(exactly = 1) { userDao.insertUser(newUser) }

        // Optionally assert that the user ID was set correctly
        assertEquals(1L, newUser.id)
    }

}