package com.wyattconrad.cs_360weighttracker.repo;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import com.wyattconrad.cs_360weighttracker.model.Goal;
import com.wyattconrad.cs_360weighttracker.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoalRepository {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private SharedPreferences sharedPreferences;
    private LiveData<Double> goalWeight;
    private final GoalDao goalDao;

    public GoalRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        goalDao = db.goalDao();
    }

    public LiveData<Double> getGoalWeight(long userId) {
        return goalDao.getGoalByUserId(userId);
    }

    // Save Goal
    public void saveGoal(Goal goal) {

        executorService.execute(() -> {
            boolean goalExists = goalDao.goalExists(goal.getUserId());
            if (goalExists) {
                goalDao.updateGoal(goal);
            } else {
                goalDao.insertGoal(goal);
            }
        });
    }

}
