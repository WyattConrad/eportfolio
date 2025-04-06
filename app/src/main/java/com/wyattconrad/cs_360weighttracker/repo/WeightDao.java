package com.wyattconrad.cs_360weighttracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wyattconrad.cs_360weighttracker.model.Weight;
import java.util.List;

@Dao
public interface WeightDao {
    @Query("SELECT * FROM weight WHERE user_id = :userid ORDER BY date_time_logged DESC")
    LiveData<List<Weight>> getWeightByUserId(long userid);

    @Query("SELECT * FROM weight WHERE id = :id")
    LiveData<Weight> getWeightById(int id);

    @Insert
    void insertWeight(Weight weight);

    @Update
    void updateWeight(Weight weight);

    @Delete
    void deleteWeight(Weight weight);

    @Query("DELETE FROM weight")
    void deleteAll();

}
