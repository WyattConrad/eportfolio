package com.wyattconrad.cs_360weighttracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.wyattconrad.cs_360weighttracker.service.LoginService;
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the settings activity layout
        setContentView(R.layout.settings_activity);

        // If the saved instance state is null, replace the fragment container with the settings fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        // Get the action bar
        ActionBar actionBar = getSupportActionBar();

        // If the action bar is not null, enable the home button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        // Declare variables
        private ActivityResultLauncher<String> requestSMSPermissionLauncher; // Used for requesting SMS permission
        private UserPreferencesService sharedPreferences;
        private LoginService loginService;
        private long userId;
        private boolean smsEnabled;
        private boolean inAppMessagingEnabled;

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Set the action bar height to 0
            int actionBarHeight = 0;

            // Get the phone number shared preference
            EditTextPreference smsNumberPreference = findPreference("sms_number");
            assert smsNumberPreference != null;

            // Create a TypedValue object to store the action bar height
            TypedValue tv = new TypedValue();
            // Check if the action bar height is available
            if (requireActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                // Get the action bar height from the TypedValue object
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }

            // Initialize permission launcher to listener for the SMS request response
            requestSMSPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            Toast.makeText(getContext(), "SMS permission granted", Toast.LENGTH_SHORT).show();
                            // Enable the phone number entry
                            smsNumberPreference.setEnabled(true);

                            // Prompt user to enter their phone number to send the sms messages to
                            // Check that the number hasn't already been populated
                            if (smsNumberPreference.getText() == null || smsNumberPreference.getText().isEmpty()) {
                                smsNumberPreference.setSummary("Permission granted! Tap here to enter phone number.");
                            } else {
                                // Number already exists, show it to user for verification
                                smsNumberPreference.setSummary(smsNumberPreference.getText());
                            }
                            // --- End Highlighting ---

                        } else {
                            Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                            // Disable the phone number entry
                            smsNumberPreference.setEnabled(false);
                        }
                    }
            );

            // Set the padding on the view to account for the action bar height
            view.setPadding(0, actionBarHeight, 0, 0);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // Initialize the shared preferences
            sharedPreferences = new UserPreferencesService(requireContext());
            loginService = new LoginService(requireContext());

            // Get the UserId
            userId = loginService.getUserId();
            smsEnabled = sharedPreferences.getBoolean(userId,"sms_enabled", false);
            inAppMessagingEnabled = sharedPreferences.getBoolean(userId, "in_app_messaging", false);

            // Log the user id and preferences
            Log.d("SettingsActivity", "User ID: " + userId);
            Log.d("SettingsActivity", "SMS Enabled: " + smsEnabled);
            Log.d("SettingsActivity", "In-app Messaging Enabled: " + inAppMessagingEnabled);

            // Set up the checkbox listener for the SMS checkbox
            SwitchPreferenceCompat smsSwitch = findPreference("sms");
            assert smsSwitch != null;
            smsSwitch.setChecked(smsEnabled);

            smsSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean isSelected = (Boolean) newValue;

                    // If the switch is selected, request the SMS permission otherwise disable it
                    if (isSelected && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestSMSPermission();
                    }
                    else if (isSelected && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        enableSMSPermission();
                    }
                    else {
                        disableSMSPermission();
                    }
                    return true;
                });

            // Get the phone number preference field
            EditTextPreference phoneNumberPreference = findPreference("sms_number");
            assert phoneNumberPreference != null;

            // Create a Shared Preferences on change listener for the phone number
            phoneNumberPreference.setOnPreferenceChangeListener((preference, newValue) -> {

                if (newValue == null) {
                    smsSwitch.setChecked(false);
                }
                // Save the phone number to shared preferences
                String phoneNumber = (String) newValue;

                // Save the phone number to shared preferences
                sharedPreferences.saveUserData(userId, "sms_number", phoneNumber);

                // Send a welcome sms message to the user
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, "Thank you for subscribing to weight tracking!", null, null);

                return true;
            });

            // Handle in-app messaging switch
            SwitchPreferenceCompat inAppMessagingSwitch = findPreference("in_app");
            assert inAppMessagingSwitch != null;
            inAppMessagingSwitch.setChecked(inAppMessagingEnabled);

            // Create a Shared Preferences on change listener for in app messaging
            inAppMessagingSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                // If the switch is selected, enable in-app messaging otherwise disable it
                boolean isSelected = (Boolean) newValue;

                // Set the value in SharedPreferences
                sharedPreferences.saveUserData(userId, "in_app_messaging", isSelected);

                return true;
            });

        }

        /***
         * Request the SMS permission
         */
        private void requestSMSPermission() {
            // Check if the SMS permission is already granted
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the SMS permission
                requestSMSPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            } else {
                // Permission already granted, set the value in SharedPreferences
                enableSMSPermission();
            }
        }

        /***
         * Enable the SMS permission
         */
        private void enableSMSPermission() {
            // Save the SMS notification preference to shared preferences
            sharedPreferences.saveUserData(userId, "sms_enabled", true);

            // Show a toast message indicating that SMS notifications are enabled
            Toast.makeText(getContext(), "SMS Enabled", Toast.LENGTH_SHORT).show();
        }

        /***
         * Disable the SMS permission
         */
        private void disableSMSPermission() {
            // Save the SMS notification preference to shared preferences
            sharedPreferences.saveUserData(userId, "sms_enabled", false);

            // Show a toast message indicating that SMS notifications are disabled
            Toast.makeText(getContext(), "SMS Disabled", Toast.LENGTH_SHORT).show();
        }
    }
}