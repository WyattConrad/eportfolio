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
package com.wyattconrad.cs_360weighttracker.ui.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.model.Goal
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the GoalScreen
 * @property goalRepository The repository for goals
 * @property loginService The service for logging in
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@HiltViewModel
class GoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    prefs: UserPreferencesService,
) : ViewModel() {

    // Get the user ID from user preferences
    val userId: Long = prefs.getGlobalLong("userId")

    val currentGoal = goalRepository.getGoalByUserId(userId)

    // Function to update the goal value in the database
    fun saveGoal(newValue: Double) = viewModelScope.launch {
        val existing = currentGoal.firstOrNull()

        val updatedGoal = if (existing == null) {
            Goal(goal = newValue, userId = userId)
        } else {
            existing.copy(goal = newValue)
        }

        goalRepository.saveGoal(updatedGoal)
    }
}