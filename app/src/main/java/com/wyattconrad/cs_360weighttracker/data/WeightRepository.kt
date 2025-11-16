package com.wyattconrad.cs_360weighttracker.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlinx.coroutines.flow.Flow

class WeightRepository(
    private val weightDao: WeightDao) : IWeightRepository {


    override suspend fun addWeight(weight: Weight) {
            // Insert the weight into the database and get the Id
            val weightId = weightDao.insertWeight(weight)
            // Set the Id of the weight to the Id returned from the database
            weight.id = weightId
    }

    override fun getWeightByUserId(userid: Long): Flow<MutableList<Weight?>?> {
        // Get the weight from the database and return it as a Flow object
        return weightDao.getWeightByUserId(userid)
    }

    override fun getAllWeightsByUserId(userId: Long): Flow<List<Weight>> {
        // Get Flow from the DAO and convert it to a Flow
        return weightDao.getWeightsByUserId(userId)
    }

    override fun getFirstWeightByUserId(userId: Long): Flow<Weight?> {
        // Get the first weight from the database and return it as a Flow object
        return weightDao.getFirstWeightByUserId(userId)
    }

    override fun getLastWeightByUserId(userId: Long): Flow<Weight?> {
        // Get the last weight from the database and return it as a Flow object
        return weightDao.getLastWeightByUserId(userId)
    }

    override fun getWeightLostByUserId(userId: Long): Flow<Double?> {
        return weightDao.getWeightLostByUserId(userId)
    }

    override fun getWeightToGoalByUserId(userId: Long): Flow<Double?> {
        return weightDao.getWeightToGoalByUserId(userId)
    }

    override suspend fun updateWeight(weight: Weight) {
        // Update the weight in the database on a background thread
        weightDao.updateWeight(weight)
    }

    override suspend fun deleteWeight(weight: Weight) {
        // Delete the weight from the database on a background thread
        weightDao.deleteWeight(weight)
    }
}
