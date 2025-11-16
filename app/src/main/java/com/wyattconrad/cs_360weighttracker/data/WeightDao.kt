package com.wyattconrad.cs_360weighttracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wyattconrad.cs_360weighttracker.model.Weight;
import java.util.List;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface WeightDao {
    @Query("SELECT * FROM weight WHERE user_id = :userid ORDER BY date_time_logged DESC")
    LiveData<List<Weight>> getWeightByUserId(long userid);

    @Query("SELECT * FROM weight WHERE user_id = :userid ORDER BY date_time_logged DESC")
    Flow<List<Weight>> getWeightsByUserId(long userid);

    @Query("SELECT * FROM weight WHERE id = :id")
    LiveData<Weight> getWeightById(int id);

    // Get the first recorded weight for a userId
    @Query("SELECT * FROM weight WHERE user_id = :userId ORDER BY date_time_logged ASC LIMIT 1")
    LiveData<Weight> getFirstWeightByUserId(long userId);

    // Get the last recorded weight for a userId
    @Query("SELECT * FROM weight WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1")
    LiveData<Weight> getLastWeightByUserId(long userId);

    // Get the total weight lost by a user
    @Query("SELECT\n" +
            "    -- First, get the weight from the earliest entry\n" +
            "    (SELECT weight FROM weight WHERE user_id = :userId ORDER BY date_time_logged ASC LIMIT 1) -\n" +
            "    -- Then, subtract the weight from the most recent entry\n" +
            "    (SELECT weight FROM weight WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1)\n" +
            "AS weight_lost\n")
    LiveData<Double> getWeightLostByUserId(long userId);

    @Query("SELECT\n" +
            "    -- First, get the most recent weight logged\n" +
            "    (SELECT weight FROM weight WHERE user_id = :userId ORDER BY date_time_logged DESC LIMIT 1)-\n" +
            "    -- Then, subtract the goal weight from the goal table\n" +
            "    (SELECT goal FROM goal WHERE user_id = :userId LIMIT 1)\n" +
            "AS weight_to_goal")
    LiveData<Double> getWeightToGoalByUserId(long userId);

    @Insert
    long insertWeight(Weight weight);

    @Update
    void updateWeight(Weight weight);

    @Delete
    void deleteWeight(Weight weight);

    @Query("DELETE FROM weight")
    void deleteAll();

}
