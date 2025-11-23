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
package com.wyattconrad.cs_360weighttracker.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

/**
 * ViewModel for the Log screen.
 * @param weightRepository Repository for managing weights.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@HiltViewModel
class LogViewModel @Inject constructor(
    private val weightRepository: WeightRepository,
    prefs: UserPreferencesService,
) : ViewModel() {

    // Get the user ID from user preferences
    val userId: Long = prefs.getGlobalLong("userId")

    // Observe the Flow of weights from the database
    val weights = weightRepository.getAllWeightsByUserId(userId)

    // Add a weight to the database
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

    // Update a weight in the database from the Edit screen
    fun updateWeight(weight: Weight, newValue: Double) {
        viewModelScope.launch {
            val updated = weight.copy(weight = newValue)
            weightRepository.updateWeight(updated)
        }
    }

    //Delete a weight from the database
    fun deleteWeight(weight: Weight) {
        viewModelScope.launch {
            weightRepository.deleteWeight(weight)
        }
    }

}