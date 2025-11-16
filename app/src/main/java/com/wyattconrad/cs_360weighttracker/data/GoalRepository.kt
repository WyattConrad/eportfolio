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


class GoalRepository(
    private val goalDao: GoalDao
) : IGoalRepository {

    // Get the goal value for a user
    override fun getGoalValue(userId: Long): Flow<Double?> {
        val goalValue = goalDao.getGoalValueByUserId(userId)
        return goalValue
    }

    // Get the goal ID for a user
    override fun getGoalId(userId: Long): Flow<Long?> {
        return goalDao.getGoalIdByUserId(userId)
    }

    // Save Goal Method used for both adding and updating goals
    override suspend fun saveGoal(goal: Goal) {
        // Execute the goal save in a background thread
        goalDao.upsertGoal(goal)
    }
}
