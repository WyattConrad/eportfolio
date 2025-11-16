package com.wyattconrad.cs_360weighttracker.data

import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlinx.coroutines.flow.Flow

interface IWeightRepository {

    suspend fun addWeight(weight: Weight)

    fun getWeightByUserId(userid: Long): Flow<MutableList<Weight?>?>

    fun getAllWeightsByUserId(userId: Long): Flow<List<Weight>>


    fun getFirstWeightByUserId(userId: Long): Flow<Weight?>

    fun getLastWeightByUserId(userId: Long): Flow<Weight?>

    fun getWeightLostByUserId(userId: Long): Flow<Double?>

    fun getWeightToGoalByUserId(userId: Long): Flow<Double?>

    suspend fun updateWeight(weight: Weight)

    suspend fun deleteWeight(weight: Weight)
}