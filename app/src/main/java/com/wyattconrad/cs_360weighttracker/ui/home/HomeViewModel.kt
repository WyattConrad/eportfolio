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
package com.wyattconrad.cs_360weighttracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

/**
 * View model for the home screen.
 * This view model is responsible for providing the data for the home screen.
 *
 * @property userRepository the user repository
 * @property goalRepository the goal repository
 * @property weightRepository the weight repository
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val weightRepository: WeightRepository,
    prefs: UserPreferencesService,
) : ViewModel() {

    // Get the user ID from user preferences
    val userId: Long = prefs.getGlobalLong("userId")

    // Observe the Flow of the user's goal from the database
    val goalState: Flow<GoalState> = goalRepository.getGoalValue(userId)
        .map { value ->
            if (value == null || value == 0.0) GoalState.NotSet
            else GoalState.Set(value)
        }


    // Observe the Flow of weights from the database
    val weights = weightRepository.getAllWeightsByUserId(userId)

    // Observe the Flow for the weight change from the database
    val weightChange = weightRepository.getWeightLostByUserId(userId)

    // Observe the Flow for the weight to goal from the database
    val weightToGoal = weightRepository.getWeightToGoalByUserId(userId)

    fun addWeight(weightValue: Double, dateTime: Instant) {
        // Launch a coroutine to perform the database operation
        viewModelScope.launch {
            val weight = Weight(
                weight = weightValue,
                userId = userId,
                dateTimeLogged = dateTime.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
            )
            weightRepository.addWeight(weight)
        }
    }

}

// Sealed class for the goal state
sealed class GoalState {
    object Loading : GoalState()
    object NotSet : GoalState()
    data class Set(val value: Double) : GoalState()
}