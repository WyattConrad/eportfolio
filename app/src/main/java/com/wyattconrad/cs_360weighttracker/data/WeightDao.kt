package com.wyattconrad.cs_360weighttracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {
    @Query("SELECT * FROM weights WHERE user_id = :userid ORDER BY date_time_logged DESC")
    fun getWeightByUserId(userid: Long): Flow<MutableList<Weight?>?>

    @Query("SELECT * FROM weights WHERE user_id = :userid ORDER BY date_time_logged DESC")
    fun getWeightsByUserId(userid: Long): Flow<List<Weight>>

    @Query("SELECT * FROM weights WHERE id = :id")
    fun getWeightById(id: Int): Flow<Weight?>

    // Get the first recorded weight for a userId
    @Query("SELECT * FROM weights WHERE user_id = :userId ORDER BY date_time_logged ASC LIMIT 1")
    fun getFirstWeightByUserId(userId: Long): Flow<Weight?>

    // Get the last recorded weight for a userId
    @Query("SELECT * FROM weights WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1")
    fun getLastWeightByUserId(userId: Long): Flow<Weight?>

    // Get the total weight lost by a user
    @Query(
        ("SELECT\n" +
                "    -- First, get the weight from the earliest entry\n" +
                "    (SELECT weight FROM weights WHERE user_id = :userId ORDER BY date_time_logged ASC LIMIT 1) -\n" +
                "    -- Then, subtract the weight from the most recent entry\n" +
                "    (SELECT weight FROM weights WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1)\n" +
                "AS weight_lost\n")
    )
    fun getWeightLostByUserId(userId: Long): Flow<Double?>

    @Query(
        ("SELECT\n" +
                "    -- First, get the most recent weight logged\n" +
                "    (SELECT weight FROM weights WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1)-\n" +
                "    -- Then, subtract the goal weight from the goal table\n" +
                "    (SELECT goal FROM goals WHERE user_id = :userId LIMIT 1)\n" +
                "AS weight_to_goal")
    )
    fun getWeightToGoalByUserId(userId: Long): Flow<Double?>

    @Insert
    suspend fun insertWeight(weight: Weight): Long

    @Update
    suspend fun updateWeight(weight: Weight)

    @Delete
    suspend fun deleteWeight(weight: Weight)

    @Query("DELETE FROM weights")
    suspend fun deleteAll()
}
