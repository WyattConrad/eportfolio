package com.wyattconrad.cs_360weighttracker.ui.goal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wyattconrad.cs_360weighttracker.model.Goal;
import com.wyattconrad.cs_360weighttracker.repo.GoalRepository;

public class GoalViewModel extends AndroidViewModel {

    private final GoalRepository goalRepository;

    public GoalViewModel(@NonNull Application application) {
        super(application);
        goalRepository = new GoalRepository(application);
    }
    public void saveGoal(Goal newGoal) {
        // Save the new goal to the database
        goalRepository.saveGoal(newGoal);
    }

    public LiveData<Double> getGoalWeight(long userId) {
        return goalRepository.getGoalWeight(userId);
    }

}