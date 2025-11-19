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

import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlinx.coroutines.flow.Flow

/**
 * Sets up the interface for the WeightRepository.
 */
interface IWeightRepository {

    // Get a Flow mutable list of weights by User ID
    fun getWeightByUserId(userid: Long): Flow<MutableList<Weight?>?>

    // Get a Flow List of weights by user ID
    fun getAllWeightsByUserId(userId: Long): Flow<List<Weight>>

    // Get the first weight by user ID
    fun getFirstWeightByUserId(userId: Long): Flow<Weight?>

    // Get the last weight by user ID
    fun getLastWeightByUserId(userId: Long): Flow<Weight?>

    // Get the weight lost by user ID
    fun getWeightLostByUserId(userId: Long): Flow<Double?>

    // Get the weight to goal by user ID
    fun getWeightToGoalByUserId(userId: Long): Flow<Double?>

    // Suspend is used for the following functions to run them on a separate thread
    // Add a weight to the database
    suspend fun addWeight(weight: Weight)

    // Update a weight in the database
    suspend fun updateWeight(weight: Weight)

    // Delete a weight from the database
    suspend fun deleteWeight(weight: Weight)
}