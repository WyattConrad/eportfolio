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
import kotlinx.coroutines.flow.map

/**
 * Implementation of the WeightRepository interface.
 * This class is responsible for providing access to the weight data in the database.
 *
 * @param weightDao The Data Access Object for the Weight entity.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
class WeightRepository(
    private val weightDao: WeightDao) : IWeightRepository {

    // Get all weights for a user ID from the database and return them as a Flow Mutable List object
    override fun getWeightByUserId(userid: Long): Flow<MutableList<Weight?>?> {
        // Get the weight from the database and return it as a Flow object
        return weightDao.getWeightByUserId(userid)
    }

    // Get all weights for a user ID from the database and return them as a Flow List object
    override fun getAllWeightsByUserId(userId: Long): Flow<List<Weight>> {
        // Get Flow from the DAO and convert it to a Flow
        return weightDao.getWeightsByUserId(userId)
    }

    // Get the first weight for a user ID from the database and return it as a Flow object
    override fun getFirstWeightByUserId(userId: Long): Flow<Weight?> {
        // Get the first weight from the database and return it as a Flow object
        return weightDao.getFirstWeightByUserId(userId)
    }

    // Get the last weight for a user ID from the database and return it as a Flow object
    override fun getLastWeightByUserId(userId: Long): Flow<Weight?> {
        // Get the last weight from the database and return it as a Flow object
        return weightDao.getLastWeightByUserId(userId)
    }

    // Get the weight lost for a user ID from the database and return it as a Flow object
    override fun getWeightLostByUserId(userId: Long): Flow<Double> {

        // Get the weight lost from the database and return it as a Flow object
        val nullableFlow: Flow<Double?> = weightDao.getWeightLostByUserId(userId)

        // If the return is null, return 0.0 in a Flow<Double>
        return nullableFlow.map { totalWeightLost ->
            totalWeightLost ?: 0.0
        }
    }

    // Get the weight to goal for a user ID from the database and return it as a Flow object
    override fun getWeightToGoalByUserId(userId: Long): Flow<Double> {

        // Get the weight to goal from the database and return it as a Flow object
        val nullableFlow: Flow<Double?> = weightDao.getWeightToGoalByUserId(userId)

        // If the return is null, return 0.0 in a Flow<Double>
        return nullableFlow.map { totalWeightToGoal ->
            totalWeightToGoal ?: 0.0
        }
    }

    // Suspend is used for the following functions to run them on a separate thread
    // Add a weight to the database and set the Id of the weight to the Id returned from the database
    override suspend fun addWeight(weight: Weight) {
        // Insert the weight into the database and get the Id
        val weightId = weightDao.insertWeight(weight)
        // Set the Id of the weight to the Id returned from the database
        weight.id = weightId
    }

    // Update the weight in the database
    override suspend fun updateWeight(weight: Weight) {
        // Update the weight in the database on a background thread
        weightDao.updateWeight(weight)
    }

    // Delete the weight from the database
    override suspend fun deleteWeight(weight: Weight) {
        // Delete the weight from the database on a background thread
        weightDao.deleteWeight(weight)
    }
}
