package com.wyattconrad.cs_360weighttracker.ui.addweight;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentAddWeightBinding;
import com.wyattconrad.cs_360weighttracker.model.Weight;
import com.wyattconrad.cs_360weighttracker.service.LoginService;
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService;

import java.util.Objects;

public class AddWeightFragment extends Fragment {

    // Declare variables
    private FragmentAddWeightBinding binding;
    private AddWeightViewModel addWeightViewModel;;
    private Button addWeightButton;
    private TextInputLayout weightEntry;
    private TextInputEditText weightEditText;
    private TextView congratsText;
    private UserPreferencesService sharedPreferences;
    private LoginService loginService;
    private long userId;
    private Double goalValue;
    private boolean smsEnabled;
    private boolean smsSent;

    private boolean inAppMessagingEnabled;


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
        loginService = new LoginService(requireContext());
        userId = loginService.getUserId();

        // Get the SMS enabled flag from SharedPreferences
        sharedPreferences = new UserPreferencesService(getContext());
        inAppMessagingEnabled = sharedPreferences.getBoolean(userId, "in_app_messaging", false);
        smsEnabled = sharedPreferences.getBoolean(userId,"sms_enabled", false);
        smsSent = sharedPreferences.getBoolean(userId, "sms_sent", false);

        // Log the user id
        Log.d("AddWeightFragment", "User ID: " + userId);
        Log.d("AddWeightFragment", "In-app messaging: " + inAppMessagingEnabled);
        Log.d("AddWeightFragment", "SMS enabled: " + smsEnabled);

        // Initialize the add weight button
        addWeightButton = binding.addWeightBtn;
        weightEntry = binding.weightEntry;
        weightEditText = binding.weightEditText;
        congratsText = binding.textCongrats;

        observeGoalValue(userId);

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
        addWeightButton.setOnClickListener(v -> {

            // Get the weight value from the TextInputLayout
            String weightText = Objects.requireNonNull(weightEntry.getEditText()).getText().toString();

            saveNewWeight(v, weightText);
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
            addWeightViewModel.insertWeight(newWeight);

            Log.d("AddWeightFragment", "Weight added to database");

            // Check if the user has reached their goal
            if (goalValue != null && weight <= goalValue) {
                Log.d("AddWeightFragment", "User has reached their goal");
                // Check if SMS is enabled
                if (smsEnabled && !smsSent) {
                    // Send an SMS to the user
                    sendSMS(weight);
                    Log.d("AddWeightFragment", "SMS sent");
                }
                // Check if in-app messaging is enabled
                if (inAppMessagingEnabled) {
                    // Send a notification to the user
                    Toast.makeText(getContext(), "GOAL REACHED! CONGRATULATIONS!", Toast.LENGTH_SHORT).show();
                    Log.d("AddWeightFragment", "In-app messaging sent");
                }
            }

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
        }
    }

    private void observeGoalValue(long userId) {
        // Observe the goal weight from the view model
        addWeightViewModel.checkGoalReached(userId).observe(getViewLifecycleOwner(), goalWeight -> goalValue = goalWeight);
    }

    private void sendSMS(double weight) {
        // Get the phone number from SharedPreferences
        String phoneNumber = sharedPreferences.getUserData(userId, "sms_number", "");
        boolean inAppMessagingEnabled = sharedPreferences.getBoolean(userId, "in_app_messaging_enabled", false);
        boolean smsEnabled = sharedPreferences.getBoolean(userId, "sms_enabled", false);
        boolean notificationSent = false;

        // Create the message
        String message = "Congratulations! You have reached your goal of " + goalValue + " lbs.";

        if(smsEnabled && !phoneNumber.isEmpty() && weight > 0){
            // Send the SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            notificationSent = true;
            sharedPreferences.saveUserData(userId, "sms_sent", true);
        }

        // Check if in-app messaging is enabled
        if (inAppMessagingEnabled && !notificationSent) {
            // Send a notification to the user
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            notificationSent = true;
        }

        if (!notificationSent) {
            // Display a message on screen
            congratsText.setText(message);
            congratsText.setVisibility(View.VISIBLE);

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