package com.wyattconrad.cs_360weighttracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wyattconrad.cs_360weighttracker.model.Goal;

@Dao
public interface GoalDao {

    @Query("SELECT goal FROM goal WHERE user_id= :userid LIMIT 1")
    LiveData<Double> getGoalByUserId(long userid);

    @Query("SELECT EXISTS (SELECT 1 FROM goal WHERE user_id= :userid)")
    boolean goalExists(long userid);

    @Insert
    void insertGoal(Goal goal);

    @Update
    void updateGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Query("DELETE FROM goal")
    void deleteAll();

}
