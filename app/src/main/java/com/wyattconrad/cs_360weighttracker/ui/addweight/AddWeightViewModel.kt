package com.wyattconrad.cs_360weighttracker.ui.addweight

import androidx.activity.result.launch
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