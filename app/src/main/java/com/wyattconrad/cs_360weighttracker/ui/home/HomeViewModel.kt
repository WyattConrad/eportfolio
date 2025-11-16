package com.wyattconrad.cs_360weighttracker.ui.home;

import static com.wyattconrad.cs_360weighttracker.service.StringService.toProperCase;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wyattconrad.cs_360weighttracker.repo.GoalRepository;
import com.wyattconrad.cs_360weighttracker.repo.UserRepository;

public class HomeViewModel extends AndroidViewModel {

    // Declare variables
    private final MutableLiveData<String> greetingText;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final MutableLiveData<Double> goal;


    public HomeViewModel(Application application) {
        super(application);
        // Initialize the user and goal repositories
        userRepository = new UserRepository(application);
        goalRepository = new GoalRepository(application);

        // Initialize the LiveData variables
        greetingText = new MutableLiveData<>();
        goal = new MutableLiveData<>();

        MutableLiveData<Double> mWeightLost = new MutableLiveData<>();
        MutableLiveData<Double> mWeightToGoal = new MutableLiveData<>();

        // Set the initial values for the LiveData variables
        mWeightLost.setValue(0.0);
        mWeightToGoal.setValue(0.0);
    }

    public LiveData<String> getText(long userId) {
        // Observe the user's first name from the user repository
        userRepository.getUserFirstName(userId).observeForever(firstName -> {
            // If the first name is null or empty, set the greeting text to "Welcome Guest"
            if (firstName == null || firstName.isEmpty()) {
                greetingText.setValue("Guest");
            }
            // Otherwise, set the greeting text to "Welcome <first name>"
            else {
                greetingText.setValue(toProperCase(firstName));
            }
        });
        // Return the LiveData variable containing the greeting text
        return greetingText;
    }


    // Get the user's goal weight
    public LiveData<Double> getGoalWeight(long userId) {
        // Observe the goal weight from the goal repository
        goalRepository.getGoalValue(userId).observeForever(goal::setValue);

        // Return the LiveData variable containing the goal weight
        return goal;
    }

}