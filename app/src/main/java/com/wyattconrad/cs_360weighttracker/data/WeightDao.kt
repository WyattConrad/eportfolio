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
import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for weight.
 * @author Wyatt Conrad
 * @version 1.0
 */
@Dao
interface WeightDao {

    // Get a mutable list of weights for a user
    @Query("SELECT * FROM weights WHERE user_id = :userid ORDER BY date_time_logged DESC")
    fun getWeightByUserId(userid: Long): Flow<MutableList<Weight?>?>

    // Get a Flow list of weights for a user
    @Query("SELECT * FROM weights WHERE user_id = :userid ORDER BY date_time_logged DESC")
    fun getWeightsByUserId(userid: Long): Flow<List<Weight>>

    /// Get a weight by its id
    @Query("SELECT * FROM weights WHERE id = :id")
    fun getWeightById(id: Int): Flow<Weight?>

    // Get the first recorded weight for a userId
    @Query("SELECT * FROM weights WHERE user_id = :userId ORDER BY date_time_logged ASC LIMIT 1")
    fun getFirstWeightByUserId(userId: Long): Flow<Weight?>

    // Get the last recorded weight for a userId
    @Query("SELECT * FROM weights WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1")
    fun getLastWeightByUserId(userId: Long): Flow<Weight?>

    // Get the total weight lost by a user
    @Query(
        ("SELECT\n" +
                "    -- First, get the weight from the earliest entry\n" +
                "    (SELECT weight FROM weights WHERE user_id = :userId ORDER BY date_time_logged ASC LIMIT 1) -\n" +
                "    -- Then, subtract the weight from the most recent entry\n" +
                "    (SELECT weight FROM weights WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1)\n" +
                "AS weight_lost\n")
    )
    fun getWeightLostByUserId(userId: Long): Flow<Double?>

    // Get the weight to goal for a user
    @Query(
        ("SELECT\n" +
                "    -- First, get the most recent weight logged\n" +
                "    (SELECT weight FROM weights WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1)-\n" +
                "    -- Then, subtract the goal weight from the goal table\n" +
                "    (SELECT goal FROM goals WHERE user_id = :userId LIMIT 1)\n" +
                "AS weight_to_goal")
    )
    fun getWeightToGoalByUserId(userId: Long): Flow<Double?>

    // Suspend is used for the following functions to run them on a separate thread
    // Add a weight to the database
    @Insert
    suspend fun insertWeight(weight: Weight): Long

    // Update a weight in the database
    @Update
    suspend fun updateWeight(weight: Weight)

    // Delete a weight from the database
    @Delete
    suspend fun deleteWeight(weight: Weight)

    // Delete all weights from the database
    @Query("DELETE FROM weights")
    suspend fun deleteAll()
}
