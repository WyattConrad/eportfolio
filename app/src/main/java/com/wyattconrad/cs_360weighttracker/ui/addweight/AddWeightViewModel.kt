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
package com.wyattconrad.cs_360weighttracker.ui.addweight

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddWeightViewModel @Inject constructor(
    private val weightRepository: WeightRepository,
    private val goalRepository: GoalRepository
) : ViewModel() {

    /**
     * Adds a new weight to the database.
     * @param weight The weight to add.
     */
    suspend fun addWeight(weight: Weight) {
        weightRepository.addWeight(weight)
    }

    // Launch a coroutine to add the weight
    fun insertWeight(weight: Weight) = viewModelScope.launch {
        addWeight(weight) // Call the suspend function from here
    }

    /**
     * Checks if the user has reached their goal.
     * @param userId The user ID.
     */
    fun checkGoalReached(userId: Long): LiveData<Double?> {
        // Get the user's goal
        return goalRepository.getGoalValue(userId).asLiveData()
    }
}