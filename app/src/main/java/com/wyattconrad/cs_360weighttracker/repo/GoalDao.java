package com.wyattconrad.cs_360weighttracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wyattconrad.cs_360weighttracker.model.Goal;
import java.util.List;

@Dao
public interface GoalDao {
    @Query("SELECT * FROM goal WHERE user_id= :userid")
    LiveData<List<Goal>> getGoalsByUserId(long userid);

    @Query("SELECT * FROM goal WHERE user_id= :userid LIMIT 1")
    LiveData<Goal> getGoalByUserId(long userid);

    @Insert
    void insertGoal(Goal goal);

    @Update
    void updateGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Query("DELETE FROM goal")
    void deleteAll();

}
