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

import com.wyattconrad.cs_360weighttracker.model.Goal
import com.wyattconrad.cs_360weighttracker.service.HashingService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Tests for the GoalRepository class.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GoalRepositoryTest {

    // Setup mock dependencies
    private lateinit var goalDao: GoalDao
    private lateinit var userDao: UserDao
    private lateinit var hashingService: HashingService
    private lateinit var goalRepository: GoalRepository
    private lateinit var userRepository: UserRepository

    // Initialize the mocks
    @Before
    fun setup() {
        MockKAnnotations.init(this)

        goalDao = mockk()
        userDao = mockk()
        hashingService = HashingService()

        goalRepository = GoalRepository(goalDao)
        userRepository = UserRepository(userDao, hashingService)
    }

    // Test the getGoalValue method
    @Test
    fun getGoalValue_returns_correct_goal_for_existing_user() = runTest {
        val userId = 1L
        val dateTimeSet = System.currentTimeMillis()
        val goalValue = 100.0.toDouble()

        val mockGoal = Goal(0L,
            dateTimeSet,
            goalValue,
            userId)

        // Mock the DAO to return a Flow emitting the mockGoal
        coEvery { goalDao.getGoalByUserId(userId) } returns flowOf(mockGoal)

        // Collect the Flow
        val result = goalRepository.getGoalByUserId(userId).first()

        assertEquals(mockGoal, result)

        coVerify(exactly = 1) { goalDao.getGoalByUserId(userId) }
    }
}