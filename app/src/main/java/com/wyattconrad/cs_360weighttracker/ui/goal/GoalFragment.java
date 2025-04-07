package com.wyattconrad.cs_360weighttracker.ui.goal;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wyattconrad.cs_360weighttracker.databinding.FragmentGoalBinding;
import com.wyattconrad.cs_360weighttracker.model.Goal;

public class GoalFragment extends Fragment {

    private GoalViewModel mViewModel;
    private FragmentGoalBinding binding;
    private SharedPreferences sharedPreferences;
    private EditText goalValue;
    private Button editBtn;
    private boolean isEditing = false;

    public static GoalFragment newInstance() {
        return new GoalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(GoalViewModel.class);

        binding = FragmentGoalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);

        goalValue = binding.goalValue;
        editBtn = binding.editBtn;

        // Setup the onClick listener for the edit button
        binding.editBtn.setOnClickListener(v -> {

            if (!isEditing) {

                String newGoal = goalValue.getText().toString();
                goalValue.setEnabled(true);
                editBtn.setText("Save");
                isEditing = true;
            } else {
                // Add methods to save the changes to the database
                saveGoal();
                goalValue.setEnabled(false);
                editBtn.setText("Edit");
                isEditing = false;
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void observeGoalText(long userId, TextView goalText) {
        // Observe the goal weight from the view model
        mViewModel.getGoalWeight(userId).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double goalWeight) {
                // If no goal weight is found, set the text to "No goal set"
                if (goalWeight == 0.0) {
                    goalValue.setText("No goal set");
                }
                // Update the goal text view with the goal weight
                else {
                    goalValue.setText("Your Goal Weight Is: " + goalWeight + " lbs");
                }
            }
        });
    }
    private void saveGoal() {
        // Get the new goal value from the EditText
        String newGoalString = goalValue.getText().toString().trim();
        double newGoalValue;
        try {
            newGoalValue = Double.parseDouble(newGoalString);
        }
        catch (NumberFormatException e) {
            // Handle the case where the input is not a valid number
            Log.d("GoalFragment", "Invalid goal value: " + newGoalString);
            goalValue.setError("Please enter a value.");
            return;
        }
        long userId;

        try {

            userId = sharedPreferences.getLong("user_id", -1);
        }
        catch (Exception e) {
            // Handle the case where the user is not logged in
            Log.d("GoalFragment", "User is not logged in");
            goalValue.setError("Please log in.");
            return;
        }

        // Create the new goal object
        Goal newGoal = new Goal(newGoalValue, userId);

        // Save the new goal value to the database
        mViewModel.saveGoal(newGoal);
    }

}