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

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import com.wyattconrad.cs_360weighttracker.utilities.TrendAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
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

    // State method to show the weight input bottom sheet
    private val _showWeightSheet = MutableStateFlow(false)
    val showWeightSheet = _showWeightSheet.asStateFlow()

    // Get the user ID from user preferences
    val userId: Long = prefs.getGlobalLong("userId")

    // Set the visibility of the weight input bottom sheet
    fun setShowWeightSheet(visible: Boolean) {
        _showWeightSheet.value = visible
    }


    // Observe the Flow of the user's goal from the database
    val goalState: StateFlow<GoalState> =
        goalRepository.getGoalValue(userId)
            .map { value ->
                when {
                    value == null || value == 0.0 -> GoalState.NotSet
                    else -> GoalState.Set(value)
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GoalState.Loading)

    // Observe the Flow of weights from the database
    val weights: StateFlow<List<Weight>> =
        weightRepository.getAllWeightsByUserId(userId)
            .map { list -> list.sortedBy { it.dateTimeLogged } } // <- ensure deterministic ordering
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // Observe the Flow for the weight change from the database
    val weightChange: StateFlow<Double> =
        weightRepository.getWeightLostByUserId(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)


    // Observe the Flow for the weight to goal from the database
    val weightToGoal: StateFlow<Double> =
        weightRepository.getWeightToGoalByUserId(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Calculate the trend data
    val trendData: StateFlow<TrendAnalysis.RegressionResult> =
        weights.map { list ->
            if (list.size > 2) {
                val result = TrendAnalysis.calculateLinearRegression(list)
                TrendAnalysis.RegressionResult(
                    slope = result.slope,
                    intercept = result.intercept,
                    trendValues = result.trendValues
                )
            } else {
                TrendAnalysis.RegressionResult()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TrendAnalysis.RegressionResult())

    // Calculate date goal may be reached
    val estimatedGoalDate: StateFlow<LocalDate?> = combine(
        goalState,
        trendData
    ) { goal, trend ->

        if (goal !is GoalState.Set) return@combine null

        val slope = trend.slope
        val intercept = trend.intercept

        if (slope == 0.0) return@combine null

        // Solve for X (epoch seconds)
        val estimatedX = ((goal.value - intercept) / slope).toLong()

        if (estimatedX <= 0) return@combine null

        Instant
            .ofEpochSecond(estimatedX)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Save a weight to the database
    fun onSaveWeight(weight: Double, dateTime: Instant) {
        // Call your existing logic
        addWeight(weight, dateTime)

        // Hide the sheet after saving
        _showWeightSheet.value = false
    }

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

    fun filterWeights(weights: List<Weight>, filter: ChartFilter): List<Weight> {
        return when (filter) {
            ChartFilter.CurrentMonth -> {
                val now = LocalDate.now()
                weights.filter { w ->
                    w.dateTimeLogged.monthValue == now.monthValue &&
                            w.dateTimeLogged.year == now.year
                }
            }
            ChartFilter.Last30 -> weights.takeLast(30)
            ChartFilter.Last100 -> weights.takeLast(100)
            ChartFilter.All -> weights
        }
    }


}

// Sealed class for the goal state
sealed class GoalState {
    object Loading : GoalState()
    object NotSet : GoalState()
    data class Set(val value: Double) : GoalState()
}

enum class ChartFilter {
    CurrentMonth,
    Last30,
    Last100,
    All
}
