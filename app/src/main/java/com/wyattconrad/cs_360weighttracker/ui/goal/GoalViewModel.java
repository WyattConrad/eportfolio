package com.wyattconrad.cs_360weighttracker.ui.goal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wyattconrad.cs_360weighttracker.model.Goal;
import com.wyattconrad.cs_360weighttracker.repo.GoalRepository;
import com.wyattconrad.cs_360weighttracker.service.LoginService;

public class GoalViewModel extends AndroidViewModel {

    // Declare variables
    private final GoalRepository goalRepository;

    // Constructor
    public GoalViewModel(@NonNull Application application) {
        super(application);
        goalRepository = new GoalRepository(application);
    }

    // Save the new goal value to the database
    public void saveGoal(Goal newGoal) {
        // Save the new goal to the database
        goalRepository.saveGoal(newGoal);
    }

    // Get the goal value for a user
    public LiveData<Double> getGoalValue(long userId) {
        // Observe the goal weight from the goal repository
        return goalRepository.getGoalValue(userId);
    }

    // Get the goal ID for a user
    public LiveData<Long> getGoalId(long userId) {
        return goalRepository.getGoalId(userId);
    }

}