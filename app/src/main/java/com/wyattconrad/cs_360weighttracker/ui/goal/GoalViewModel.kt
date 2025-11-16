package com.wyattconrad.cs_360weighttracker.ui.goal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.model.Goal
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : ViewModel() {

    // Save the new goal value to the database
    suspend fun saveGoal(newGoal: Goal) {
        // Save the new goal to the database
        goalRepository.saveGoal(newGoal)
    }

    // Function to update the goal value in the database
    fun saveGoalCoroutine(goal: Goal) {
        viewModelScope.launch {
            goalRepository.saveGoal(goal)
        }
    }

    // Get the goal value for a user
    fun getGoalValue(userId: Long): LiveData<Double?> {
        // Observe the goal weight from the goal repository
        return goalRepository.getGoalValue(userId).asLiveData()
    }

    // Get the goal ID for a user
    fun getGoalId(userId: Long): LiveData<Long?> {
        return goalRepository.getGoalId(userId).asLiveData()
    }
}