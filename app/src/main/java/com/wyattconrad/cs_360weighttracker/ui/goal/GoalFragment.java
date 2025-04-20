package com.wyattconrad.cs_360weighttracker.ui.goal;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentGoalBinding;
import com.wyattconrad.cs_360weighttracker.model.Goal;
import com.wyattconrad.cs_360weighttracker.service.LoginService;
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService;

public class GoalFragment extends Fragment {

    // Declare variables
    private GoalViewModel mViewModel;
    private FragmentGoalBinding binding;
    private LoginService loginService;
    private UserPreferencesService sharedPreferences;
    private EditText goalValue;
    private Button editBtn;
    private Button changeBtn;
    private long goalId;
    private boolean inAppMessagingEnabled;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // Inflate the layout for this fragment
        binding = FragmentGoalBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    // Override the onViewCreated method
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Initialize the view model
        mViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        // Initialize the login service
        loginService = new LoginService(requireContext());
        long userId = loginService.getUserId();

        sharedPreferences = new UserPreferencesService(requireContext());
        inAppMessagingEnabled = sharedPreferences.getBoolean(userId, "in_app_messaging", false);

        // Initialize the goal value and edit button
        goalValue = binding.goalValue;
        editBtn = binding.editBtn;
        changeBtn = binding.changeBtn;

        // Get the user's goal weight
        observeUserGoal(loginService.getUserId());

        // Setup the onClick listener for the change button
        assert binding.changeBtn != null;
        binding.changeBtn.setOnClickListener(v -> {
            editBtn.setVisibility(View.VISIBLE);
            changeBtn.setVisibility(View.GONE);
            goalValue.setEnabled(true);
            goalValue.requestFocus();
            goalValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        });

        // Setup the onClick listener for the edit button
        binding.editBtn.setOnClickListener(v -> {
            saveGoal();
        });

    }

    // Override the onDestroyView method
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Get the user's goal weight
    public void observeUserGoal(long userId) {
        // Observe the goal weight from the goal repository
        mViewModel.getGoalValue(userId).observeForever(userGoalFromDb -> {
            // If the goal weight is null, set it to 0.0
            if (userGoalFromDb == null) {
                userGoalFromDb = 0.0;
            }
            // Update the goal value EditText with the user's goal weight
            goalValue.setText(String.valueOf(userGoalFromDb));
        });
    }

    // Method to save the new goal value to the database
    private void saveGoal() {
        // Get the new goal value from the EditText
        String newGoalString = goalValue.getText().toString().trim();

        // Declare a new goal variable
        double newGoalValue;
        // Try to parse the input string as a double
        try {
            newGoalValue = Double.parseDouble(newGoalString);
        }
        catch (NumberFormatException e) {
            // Handle the case where the input is not a valid number
            Log.d("GoalFragment", "Invalid goal value: " + newGoalString);
            goalValue.setError("Please enter a value.");
            return;
        }

        // Get the user ID from the login service
        long userId;

        // Try to get the user ID from the login service
        try {
            userId = loginService.getUserId();
        }
        catch (Exception e) {
            // Handle the case where the user is not logged in
            Log.d("GoalFragment", "User is not logged in");
            goalValue.setError("Please log in.");
            return;
        }

        // Create the new goal object
        Goal newGoal = new Goal(newGoalValue, userId);
        newGoal.setId(goalId);

        // Notify the user that the goal has been updated
        if (inAppMessagingEnabled) {
            Toast.makeText(getContext(), "Goal updated", Toast.LENGTH_SHORT).show();
        }

        // Hide the edit button and show the change button
        editBtn.setVisibility(View.GONE);
        changeBtn.setVisibility(View.VISIBLE);
        goalValue.setEnabled(false);
        goalValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));

        // Save the new goal value to the database
        mViewModel.saveGoal(newGoal);

        // Reset the SMS sent flag
        sharedPreferences.saveUserData(userId, "sms_sent", false);
    }

}