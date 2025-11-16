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
import com.wyattconrad.cs_360weighttracker.service.LoginService
import com.wyattconrad.cs_360weighttracker.utilities.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for the LogFragment.
 * @param weightRepository Repository for managing weights.
 * @param loginService Service for handling user login.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@HiltViewModel
class LogViewModel @Inject constructor(// Inject your repository, which in turn uses your DAO
    private val weightRepository: WeightRepository,
    loginService: LoginService
) : ViewModel() {

    // Get the user ID from the login service
    val userId: Long = loginService.userId

    // Setup a channel for sending UI events
    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // Observe the Flow of weights from the database
    val weights = weightRepository.getAllWeightsByUserId(userId)

    // Add a weight to the database
    fun addWeight(weightValue: Double) {
        // Launch a coroutine to perform the database operation
        viewModelScope.launch {
            val weight = Weight(weight = weightValue, userId = userId)
            weightRepository.addWeight(weight)
        }
    }

}

// Sealed class to define the events that can be sent from the UI
sealed class LogEvent {
    data class DeleteWeight(val weight: Weight) : LogEvent()
    data class EditWeight(val weight: Weight) : LogEvent()
    data class UpdateWeight(val newWeightValue: String) : LogEvent()
    object DismissEditDialog : LogEvent()
}