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

class WeightRepository(
    private val weightDao: WeightDao) : IWeightRepository {


    override suspend fun addWeight(weight: Weight) {
            // Insert the weight into the database and get the Id
            val weightId = weightDao.insertWeight(weight)
            // Set the Id of the weight to the Id returned from the database
            weight.id = weightId
    }

    override fun getWeightByUserId(userid: Long): Flow<MutableList<Weight?>?> {
        // Get the weight from the database and return it as a Flow object
        return weightDao.getWeightByUserId(userid)
    }

    override fun getAllWeightsByUserId(userId: Long): Flow<List<Weight>> {
        // Get Flow from the DAO and convert it to a Flow
        return weightDao.getWeightsByUserId(userId)
    }

    override fun getFirstWeightByUserId(userId: Long): Flow<Weight?> {
        // Get the first weight from the database and return it as a Flow object
        return weightDao.getFirstWeightByUserId(userId)
    }

    override fun getLastWeightByUserId(userId: Long): Flow<Weight?> {
        // Get the last weight from the database and return it as a Flow object
        return weightDao.getLastWeightByUserId(userId)
    }

    override fun getWeightLostByUserId(userId: Long): Flow<Double?> {
        return weightDao.getWeightLostByUserId(userId)
    }

    override fun getWeightToGoalByUserId(userId: Long): Flow<Double?> {
        return weightDao.getWeightToGoalByUserId(userId)
    }

    override suspend fun updateWeight(weight: Weight) {
        // Update the weight in the database on a background thread
        weightDao.updateWeight(weight)
    }

    override suspend fun deleteWeight(weight: Weight) {
        // Delete the weight from the database on a background thread
        weightDao.deleteWeight(weight)
    }
}
