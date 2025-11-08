package com.wyattconrad.cs_360weighttracker.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;
import com.wyattconrad.cs_360weighttracker.model.Goal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoalRepository {

    // Declare variables
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final GoalDao goalDao;

    // Constructor
    public GoalRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        goalDao = db.goalDao();
    }

    // Constructor for testing purposes
    // Uses dependency injection for the goalDao so it can be mocked
    public GoalRepository(GoalDao goalDao) {
        this.goalDao = goalDao;
    }


    // Get the goal value for a user
    public LiveData<Double> getGoalValue(long userId) {
        LiveData<Double> goalValue = goalDao.getGoalValueByUserId(userId);
        if (goalValue == null) {
            return new LiveData<>() {
                @Override
                protected void onActive() {
                    super.onActive();
                    Goal goal = new Goal(0.0, userId);
                    goalDao.insertGoal(goal);
                }
            };
        }
        return goalValue;
    }

    // Get the goal ID for a user
    public LiveData<Long> getGoalId(long userId) {
        return goalDao.getGoalIdByUserId(userId);
    }

    // Save Goal Method used for both adding and updating goals
    public void saveGoal(Goal goal) {

        // Execute the goal save in a background thread
        executorService.execute(() -> {
            // Check if the goal already exists for the user
            Goal existingGoal = goalDao.getGoalByUserId(goal.getUserId());
            // If the goal exists, update it, otherwise insert it
            if (existingGoal != null) {
                // Update the existing goal with the new value and date/time set
                existingGoal.setGoal(goal.getGoal());
                existingGoal.setDateTimeSet(System.currentTimeMillis());
                // Update the existing goal in the database
                goalDao.updateGoal(existingGoal);
            // If the goal does not exist, insert it into the database
            } else {
                goalDao.insertGoal(goal);
            }
        });
    }

}
