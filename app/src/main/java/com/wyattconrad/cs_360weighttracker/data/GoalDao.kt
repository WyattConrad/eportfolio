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
import androidx.room.Upsert
import com.wyattconrad.cs_360weighttracker.model.Goal
import kotlinx.coroutines.flow.Flow


/**
 * Data access object for goals.
 * @author Wyatt Conrad
 * @version 1.0
 */
@Dao
interface GoalDao {

    // Get the goal value for a user.
    @Query("SELECT goal FROM goals WHERE user_id= :userid LIMIT 1")
    fun getGoalValueByUserId(userid: Long): Flow<Double?>

    // Check if a goal exists for a user.
    @Query("SELECT EXISTS (SELECT 1 FROM goals WHERE user_id= :userid)")
    fun goalExists(userid: Long): Boolean

    // Get the goal id for a user.
    @Query("SELECT id FROM goals WHERE user_id= :userid LIMIT 1")
    fun getGoalIdByUserId(userid: Long): Flow<Long?>

    // Get the goal for a user.
    @Query("SELECT * FROM goals WHERE user_id= :userid")
    fun getGoalByUserId(userid: Long): Flow<Goal?>

    // Suspend is used for the following functions to run them on a separate thread
    // Insert a new goal
    @Insert
    suspend fun insertGoal(goal: Goal)

    // Update a goal
    @Update
    suspend fun updateGoal(goal: Goal)

    // Upsert a goal
    @Upsert
    suspend fun upsertGoal(goal: Goal)

    // Delete a goal
    @Delete
    suspend fun deleteGoal(goal: Goal)

    // Delete all goals
    @Query("DELETE FROM goals")
    suspend fun deleteAll()
}
