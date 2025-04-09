package com.wyattconrad.cs_360weighttracker.ui.addweight;

import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentAddWeightBinding;
import com.wyattconrad.cs_360weighttracker.model.Weight;

import java.util.Objects;

public class AddWeightFragment extends Fragment {

    // Declare variables
    private FragmentAddWeightBinding binding;
    private AddWeightViewModel addWeightViewModel;;
    private Button addWeightButton;
    private TextInputLayout weightEntry;
    private TextInputEditText weightEditText;
    private SharedPreferences sharedPreferences;
    private long userId;
    private boolean inAppMessagingEnabled;

    // Create a new instance of the fragment
    public static AddWeightFragment newInstance() {
        return new AddWeightFragment();
    }

    // Override the onCreateView method to initialize the view
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Initialize the ViewModel
        addWeightViewModel = new ViewModelProvider(this).get(AddWeightViewModel.class);

        // Initialize the view binding
        binding = FragmentAddWeightBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the user ID from SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getLong("user_id", -1);
        inAppMessagingEnabled = sharedPreferences.getBoolean("in_app_messaging", false);

        // Initialize the add weight button
        addWeightButton = binding.addWeightBtn;
        weightEntry = binding.weightEntry;
        weightEditText = binding.weightEditText;

        // Create a listener for the weight edit text
        weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable the add weight button if the weight edit text is not empty
                addWeightButton.setEnabled(s.length() > 0);

                // Check the value of the weight edit text
                String weightText = s.toString();
                try {
                    // Check for decimal points
                    checkDecimals(weightText);

                    // Validate the weight
                    validateWeight(weightText);

                } catch (NumberFormatException e) {
                    // Display an error via toast (if enabled) and on the field
                    if (inAppMessagingEnabled) {
                        Toast.makeText(getContext(), "Invalid weight value", Toast.LENGTH_SHORT).show();
                    }
                    weightEntry.setError("Invalid weight value");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Set the click listener for the add weight button
        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the weight value from the TextInputLayout
                String weightText = Objects.requireNonNull(weightEntry.getEditText()).getText().toString();

                saveNewWeight(v, weightText);
            }
        });

        return root;
    }

    private void saveNewWeight(View v, String weightText) {
        // Process the entry if the weight is valid
        try {
            double weight = Double.parseDouble(weightText);

            // Create a new weight object
            Weight newWeight = new Weight(weight, userId);

            // Add the new weight to the database
            addWeightViewModel.addWeight(newWeight);

            // Show a toast message to indicate that the weight has been added
            if (inAppMessagingEnabled) {
                Toast.makeText(getContext(), "Weight added", Toast.LENGTH_SHORT).show();
            }

            // Navigate back to the home screen
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_home);

        }
        catch (NumberFormatException e) {
            // Show a toast message to indicate that the weight value is invalid
            if (inAppMessagingEnabled) {
                Toast.makeText(getContext(), "Invalid weight value", Toast.LENGTH_SHORT).show();
            }
            // Set an error on the field
            weightEntry.setError("Invalid weight value");
            return;
        }
    }

    private void validateWeight(String weightText) {
        // Convert the weight text to a double and process the value
        double weight = Double.parseDouble(weightText);
        if (weight <= 0) {
            // Set an error on the field
            if (inAppMessagingEnabled) {
                Toast.makeText(getContext(), "Weight must be greater than 0", Toast.LENGTH_SHORT).show();
            }
            weightEntry.setError("Weight must be greater than 0");

        } else if (weight > 999) {
            // Set an error on the field
            if (inAppMessagingEnabled) {
                Toast.makeText(getContext(), "Weight must be less than 1000", Toast.LENGTH_SHORT).show();
            }
            weightEntry.setError("Weight must be less than 1000");
        } else {
            // Clear the error on the field
            weightEntry.setError(null);
        }
    }

    private void checkDecimals(String weightText){
        // Check decimal points are 2 or less
        if (weightText.contains(".")) {
            int index = weightText.indexOf(".");
            if (index >= 0 && index < weightText.length() - 1) {
                int decimals = weightText.length() - index - 1;
                if (decimals > 2) {
                    // Modify the text to remove the extra decimals
                    String newText = weightText.substring(0, index + 3);
                    weightEditText.setText(newText);
                    weightEditText.setSelection(newText.length());

                }
            }
        }
    }
}