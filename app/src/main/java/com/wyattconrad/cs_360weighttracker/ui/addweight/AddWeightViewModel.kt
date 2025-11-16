package com.wyattconrad.cs_360weighttracker.ui.addweight;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wyattconrad.cs_360weighttracker.model.Weight;
import com.wyattconrad.cs_360weighttracker.repo.GoalRepository;
import com.wyattconrad.cs_360weighttracker.repo.WeightRepository;

public class AddWeightViewModel extends AndroidViewModel {

    // Declare variables
    private final WeightRepository weightRepository;
    private final GoalRepository goalRepository;

    /**
     * Constructor for the AddWeightViewModel class.
     * @param application The application context.
     */
    public AddWeightViewModel(Application application) {
        super(application);
        // Initialize the repositories
        weightRepository = new WeightRepository(application);
        goalRepository = new GoalRepository(application);
    }

    /**
     * Adds a new weight to the database.
     * @param weight The weight to add.
     */
    public void addWeight(Weight weight) {
        weightRepository.addWeight(weight);
    }

    /**
     * Checks if the user has reached their goal.
     * @param userId The user ID.
     */
    public LiveData<Double> checkGoalReached(long userId) {
        // Get the user's goal
        return goalRepository.getGoalValue(userId);
    }
}