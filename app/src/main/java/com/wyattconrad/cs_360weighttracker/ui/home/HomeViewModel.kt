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

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.R
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.SMSService
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import com.wyattconrad.cs_360weighttracker.utilities.TrendAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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
    private val smsService: SMSService,
    prefs: UserPreferencesService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // State method to show the weight input bottom sheet
    private val _showWeightSheet = MutableStateFlow(false)
    val showWeightSheet = _showWeightSheet.asStateFlow()

    // SharedFlow to hold events
    private val _events = MutableSharedFlow<HomeEvent>()
    val events = _events.asSharedFlow()


    // State method to set the filter
    private val _filter = MutableStateFlow(ChartFilter.Last30)
    val filter: StateFlow<ChartFilter> = _filter
    fun setFilter(f: ChartFilter) { _filter.value = f }

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
    @OptIn(ExperimentalCoroutinesApi::class)
    val weights: StateFlow<List<Weight>> =
        filter.flatMapLatest { filterValue ->
            weightRepository.getAllWeightsByUserId(userId, filterValue)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    // Observe the Flow for the weight change from the database
    val weightChange: StateFlow<Double> =
        weightRepository.getWeightLostByUserId(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)


    // Observe the Flow for the weight to goal from the database
    val weightToGoal: StateFlow<Double?> =
        weightRepository.getWeightToGoalByUserId(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)


    // Calculate the trend data
    val trendData: StateFlow<TrendAnalysis.RegressionResult> =
        weights.map { list ->
            val ordered = list.sortedBy { it.dateTimeLogged }

            if (ordered.size > 2) {
                val result = TrendAnalysis.calculateLinearRegression(ordered)
                TrendAnalysis.RegressionResult(
                    slope = result.slope,
                    intercept = result.intercept,
                    trendValues = result.trendValues
                )
            } else {
                TrendAnalysis.RegressionResult()
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TrendAnalysis.RegressionResult()
        )
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

    // Initialize the view model
    init {
        viewModelScope.launch {
            // Collect the weightToGoal flow
            weightToGoal.collect { difference ->

                // 1. Check if difference is not null (meaning data has loaded)
                // 2. Check if difference <= 0 (meaning goal is met or exceeded)
                if (difference != null && difference <= 0.0) {

                    // Double check that a goal is actually set using your existing goalState
                    if (goalState.value is GoalState.Set) {
                        // GOAL REACHED!
                        if (prefs.getGlobalBoolean("sms_enabled", false) && prefs.getGlobalString("sms_number") != null) {
                            smsService.sendSMS(prefs.getGlobalString("sms_number") ?: "", context.getString(R.string.congrats))
                        }

                        // Emit the event
                        _events.emit(HomeEvent.GoalReached)

                    }
                }
            }
        }
    }

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
                dateTimeLogged = dateTime.atZone(ZoneId.systemDefault()).toLocalDateTime()
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

// Class to hold home events
sealed class HomeEvent {
    object GoalReached : HomeEvent()
}

// Enum for the chart filter
enum class ChartFilter {
    CurrentMonth,
    Last30,
    Last100,
    All
}
