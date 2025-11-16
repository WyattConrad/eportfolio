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
import kotlinx.coroutines.flow.Flow

/**
 * Sets up the interface for the GoalRepository.
 */
interface IGoalRepository {

    // Get the goal value for a user
    fun getGoalValue(userId: Long): Flow<Double?>

    // Get the goal ID for a user
    fun getGoalId(userId: Long): Flow<Long?>

    // Save a goal
    suspend fun saveGoal(goal: Goal)


}