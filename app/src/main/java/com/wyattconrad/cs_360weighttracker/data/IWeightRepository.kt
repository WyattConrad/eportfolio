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

interface IWeightRepository {

    suspend fun addWeight(weight: Weight)

    fun getWeightByUserId(userid: Long): Flow<MutableList<Weight?>?>

    fun getAllWeightsByUserId(userId: Long): Flow<List<Weight>>


    fun getFirstWeightByUserId(userId: Long): Flow<Weight?>

    fun getLastWeightByUserId(userId: Long): Flow<Weight?>

    fun getWeightLostByUserId(userId: Long): Flow<Double?>

    fun getWeightToGoalByUserId(userId: Long): Flow<Double?>

    suspend fun updateWeight(weight: Weight)

    suspend fun deleteWeight(weight: Weight)
}