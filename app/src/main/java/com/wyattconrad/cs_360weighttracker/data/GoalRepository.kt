package com.wyattconrad.cs_360weighttracker.data

import com.wyattconrad.cs_360weighttracker.model.Goal
import kotlinx.coroutines.flow.Flow


class GoalRepository(
    private val goalDao: GoalDao
) : IGoalRepository {

    // Get the goal value for a user
    override fun getGoalValue(userId: Long): Flow<Double?> {
        val goalValue = goalDao.getGoalValueByUserId(userId)
        return goalValue
    }

    // Get the goal ID for a user
    override fun getGoalId(userId: Long): Flow<Long?> {
        return goalDao.getGoalIdByUserId(userId)
    }

    // Save Goal Method used for both adding and updating goals
    override suspend fun saveGoal(goal: Goal) {
        // Execute the goal save in a background thread
        goalDao.upsertGoal(goal)
    }
}
