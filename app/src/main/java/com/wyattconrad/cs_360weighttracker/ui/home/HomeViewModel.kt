package com.wyattconrad.cs_360weighttracker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.StringService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val weightRepository: WeightRepository
) : ViewModel() {

    fun getWeightByUserId(id: Long): LiveData<MutableList<Weight?>?> {
        return weightRepository.getWeightByUserId(id).asLiveData()
    }

    suspend fun updateWeight(weight: Weight) {
        weightRepository.updateWeight(weight)
    }

    suspend fun deleteWeight(weight: Weight) {
        weightRepository.deleteWeight(weight)
    }

    fun getWeightLostByUserId(id: Long): LiveData<Double?> {
        return weightRepository.getWeightLostByUserId(id).asLiveData()
    }

    fun getWeightToGoalByUserId(userId: Long): LiveData<Double?> {
        return weightRepository.getWeightToGoalByUserId(userId).asLiveData()
    }

    fun getText(userId: Long): LiveData<String?> {
        // Observe the user's first name from the user repository
        return userRepository.getUserFirstName(userId).map { firstName: String? ->
            if (firstName.isNullOrBlank()){
                "Guest"
            } else {
                StringService.toProperCase(firstName)
            }
        }.asLiveData()
    }


    // Get the user's goal weight
    fun getGoalWeight(userId: Long): LiveData<Double?> {
        // Observe the goal weight from the goal repository
        return goalRepository.getGoalValue(userId).map { goalValue: Double? ->
            when {
                goalValue == null -> 0.0
                goalValue.isNaN() -> 0.0
                else -> goalValue
            }
        }.asLiveData()
    }
}