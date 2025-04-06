package com.wyattconrad.cs_360weighttracker.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.adapter.WeightAdapter;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentHomeBinding;
import com.wyattconrad.cs_360weighttracker.viewmodel.WeightListViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    // Declare variables
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private WeightAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textView;
    private long UserId;
    private String userFirstName;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize the home and weightlist view models
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        WeightListViewModel weightListViewModel = new ViewModelProvider(this).get(WeightListViewModel.class);

        // Get the user's ID from SharedPreferences, if one doesn't exist, set it to -1
        SharedPreferences preferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        UserId = preferences.getLong("user_id", -1);

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the goal text view from the layout
        final TextView goalText = binding.goalText;

        // Observe the greeting text from the view model
        observeUserFirstName(1);

        // Set up the recycler view to display the recorded weights list
        recyclerView = binding.listArea;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the adapter for the recycler view
        adapter = new WeightAdapter();
        recyclerView.setAdapter(adapter);

        // Get the user's weights from the view model
        if (UserId != -1) {
            weightListViewModel.getWeightByUserId(UserId);
        }
        // TODO: REMOVE ELSE STATEMENT AFTER LOGIN FULLY IMPLEMENTED
        //  If no user ID is found, set it to 1
        else {
            UserId = 1;
        }

        // Observe the goal text from the view model
        observeGoalText(UserId, goalText);

        // Observe the LiveData and update the adapter when the data changes
        weightListViewModel.getWeightByUserId(1).observe(getViewLifecycleOwner(), weights -> {
            // Update the adapter with the user's weights
            if (weights != null && !weights.isEmpty()) {
                adapter.setWeightList(weights);
            }
            // If no weights are found, set the adapter to an empty list
            else {
                adapter.setWeightList(new ArrayList<>());
            }
        });

        // Set up the FAB click listener
        setFABClickListener(root);

        // Return the root view
        return root;
    }

    private static void setFABClickListener(View root) {
        // Get the FAB from the layout
        FloatingActionButton fab = root.findViewById(R.id.fab);
        // Set the click listener for the FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the AddWeightFragment when the FAB is clicked
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.navigation_addweight);
            }
        });
    }

    private void observeGoalText(long userId, TextView goalText) {
        // Observe the goal weight from the view model
        homeViewModel.getGoalWeight(userId).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double goalWeight) {
                // If no goal weight is found, set the text to "No goal set"
                if (goalWeight == null) {
                    goalText.setText("No goal set");
                }
                // Update the goal text view with the goal weight
                else {
                    goalText.setText("Your Goal Weight Is: " + goalWeight + " lbs");
                }
            }
        });
    }

    private void observeUserFirstName(long userId) {
        // Observe the user's first name from the view model
        homeViewModel.getText(userId).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String welcomeMessage) {
                // Update the action bar title with the user's first name
                userFirstName = welcomeMessage;
                requireActivity().setTitle(userFirstName);
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Not Implemented Yet
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear the binding
        binding = null;
    }
}