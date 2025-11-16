package com.wyattconrad.cs_360weighttracker.data

import androidx.lifecycle.LiveData
import com.wyattconrad.cs_360weighttracker.model.Goal
import kotlinx.coroutines.flow.Flow

interface IGoalRepository {

    fun getGoalValue(userId: Long): Flow<Double?>

    fun getGoalId(userId: Long): Flow<Long?>

    suspend fun saveGoal(goal: Goal)


}