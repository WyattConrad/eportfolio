package com.wyattconrad.cs_360weighttracker.ui.log;

import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.adapter.WeightAdapter;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentLogBinding;
import com.wyattconrad.cs_360weighttracker.service.LoginService;
import com.wyattconrad.cs_360weighttracker.viewmodel.WeightListViewModel;

import java.util.ArrayList;

public class LogFragment extends Fragment {


    private WeightAdapter adapter;
    private FragmentLogBinding binding;

    public static LogFragment newInstance() {
        return new LogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLogBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogViewModel mViewModel;

        mViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        // TODO: Use the ViewModel

        LoginService loginService;

        // Get the user's ID from SharedPreferences, if one doesn't exist, set it to -1
        loginService = new LoginService(requireContext());
        long userId = loginService.getUserId();

        WeightListViewModel weightListViewModel = new ViewModelProvider(this).get(WeightListViewModel.class);

        // Initialize the recycler view
        RecyclerView recyclerView;

        // Set up the recycler view to display the recorded weights list
        recyclerView = binding.listArea;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the adapter for the recycler view
        adapter = new WeightAdapter(new Application());
        recyclerView.setAdapter(adapter);

        // Get the user's weights from the view model
        weightListViewModel.getWeightByUserId(userId);

        // Observe the LiveData and update the adapter when the data changes
        weightListViewModel.getWeightByUserId(userId).observe(getViewLifecycleOwner(), weights -> {
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
        setFABClickListener(view);

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

}