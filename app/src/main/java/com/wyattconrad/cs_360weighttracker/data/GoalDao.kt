package com.wyattconrad.cs_360weighttracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.wyattconrad.cs_360weighttracker.model.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT goal FROM goals WHERE user_id= :userid LIMIT 1")
    fun getGoalValueByUserId(userid: Long): Flow<Double?>

    @Query("SELECT EXISTS (SELECT 1 FROM goals WHERE user_id= :userid)")
    fun goalExists(userid: Long): Boolean

    @Query("SELECT id FROM goals WHERE user_id= :userid LIMIT 1")
    fun getGoalIdByUserId(userid: Long): Flow<Long?>

    @Query("SELECT * FROM goals WHERE user_id= :userid")
    fun getGoalByUserId(userid: Long): Goal?

    @Insert
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Upsert
    suspend fun upsertGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("DELETE FROM goals")
    suspend fun deleteAll()
}
