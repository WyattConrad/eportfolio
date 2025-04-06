package com.wyattconrad.cs_360weighttracker.ui.goal;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentGoalBinding;

public class GoalFragment extends Fragment {

    private GoalViewModel mViewModel;
    private FragmentGoalBinding binding;
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

}