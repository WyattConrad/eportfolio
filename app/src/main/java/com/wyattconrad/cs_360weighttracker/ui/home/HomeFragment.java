package com.wyattconrad.cs_360weighttracker.ui.home;

import static com.wyattconrad.cs_360weighttracker.service.StringService.toProperCase;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.adapter.WeightAdapter;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentHomeBinding;
import com.wyattconrad.cs_360weighttracker.model.Weight;
import com.wyattconrad.cs_360weighttracker.service.LoginService;
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService;
import com.wyattconrad.cs_360weighttracker.service.WeightService;
import com.wyattconrad.cs_360weighttracker.viewmodel.WeightListViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    // Declare variables
    private HomeViewModel homeViewModel;
    private UserPreferencesService sharedPreferences;

    private FragmentHomeBinding binding;
    private TextView weightLostHeader;
    private TextView weightLost;
    private TextView weightToGoalHeader;
    private TextView weightToGoal;
    private TextView goalText;
    private Double goalValue;
    private long userId;
    private String userFirstName;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the login service
        LoginService loginService;

        // Initialize the home and weightlist view models
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        WeightListViewModel weightListViewModel = new ViewModelProvider(this).get(WeightListViewModel.class);

        // Get the user's ID from SharedPreferences, if one doesn't exist, set it to -1
        loginService = new LoginService(requireContext());
        userId = loginService.getUserId();

        sharedPreferences = new UserPreferencesService(getContext());
        userFirstName = sharedPreferences.getUserData(userId, "user_first_name", "Guest");

        // Get the goal, weight, and weight loss percentage text view from the layout
        goalText = binding.goalText;
        weightLost = binding.weightLost;
        weightToGoal = binding.weightToGoal;
        weightLostHeader = binding.weightLostHeader;
        weightToGoalHeader = binding.weightToGoalHeader;

        if (userId == -1) {
            // Observe the greeting text from the view model
            observeUserFirstName(userId);
        }

        // Log the user id and first name
        Log.d("HomeFragment", "User ID: " + userId);
        Log.d("HomeFragment", "User First Name: " + userFirstName);


        // Get the user's weights from the view model
        weightListViewModel.getWeightByUserId(userId);

        // Observe the goal text from the view model
        observeGoalText(userId);

        // Initialize the Weight Service
        WeightService weightService = new WeightService();


        // Observe the LiveData and update the adapter when the data changes
        weightListViewModel.getWeightByUserId(userId).observe(getViewLifecycleOwner(), weights -> {
            // Update the adapter with the user's weights
            if (weights != null && goalValue != null && !weights.isEmpty()) {

                // Get and Update the weight to goal
                double weightToGoalValue = weightService.calculateWeightToGoal(weights, goalValue);
                double weightLostValue = weightService.calculateWeightLoss(weights);

                // If weight to goal is negative, set the text to "Less Than Goal" and make it positive
                if (weightToGoalValue < 0) {
                    weightToGoalValue *= -1;
                    weightToGoalHeader.setText(R.string.LessThanGoal);
                }

                // If weight lost is negative, set the text to "Weight Gained" and make it positive
                if (weightLostValue < 0) {
                    weightLostHeader.setText(R.string.WeightGained);
                    weightLostValue *= -1;
                }

                // Update the values of the text boxes
                weightToGoal.setText(String.valueOf(weightToGoalValue));
                weightLost.setText(String.valueOf(weightLostValue));

            }
            // If no weights are found, set the adapter to an empty list
            else {
                //TODO: Add blank trend analysis adapter here
            }
        });

        // Set up the FAB click listener
        setFABClickListener(view);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Get the user's first name from SharedPreferences
        sharedPreferences = new UserPreferencesService(getContext());
        userFirstName = sharedPreferences.getUserData(userId, "user_first_name", "Guest");
        requireActivity().setTitle(userFirstName);
        // Update the action bar title with the user's first name
        new Handler(Looper.getMainLooper()).post(() -> {
            ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Welcome " + toProperCase(userFirstName));
            }
        });
    }

    private static void setFABClickListener(View root) {
        // Get the FAB from the layout
        FloatingActionButton fab = root.findViewById(R.id.fab);
        // Set the click listener for the FAB
        fab.setOnClickListener(view -> {
            // Navigate to the AddWeightFragment when the FAB is clicked
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.navigation_addweight);
        });
    }

    private void observeGoalText(long userId) {
        // Observe the goal weight from the view model
        homeViewModel.getGoalWeight(userId).observe(getViewLifecycleOwner(), goalWeight -> {
            // If no goal weight is found, set the text to "No goal set"
            if (goalWeight == null || goalWeight == 0) {
                Log.d("HomeFragment", "No goal set");
                goalText.setText(R.string.no_goal_set);
                goalValue = 0.0;
            }
            // Update the goal text view with the goal weight
            else {
                goalText.setText(String.format("Your Goal Weight Is: %s lbs", goalWeight));
                goalValue = goalWeight;
            }
        });
    }

    private void observeUserFirstName(long userId) {
        // Observe the user's first name from the view model
        homeViewModel.getText(userId).observe(getViewLifecycleOwner(), welcomeMessage -> {
            // Update the action bar title with the user's first name
            userFirstName = welcomeMessage;
            requireActivity().setTitle(userFirstName);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear the binding
        binding = null;
    }
}