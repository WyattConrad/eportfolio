package com.wyattconrad.cs_360weighttracker;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
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

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private static final int SMS_PERMISSION_REQUEST_CODE = 1;
        private ActivityResultLauncher<String> requestSMSPermissionLauncher; // Used for requesting SMS permission
        private SharedPreferences sharedPreferences;

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Set the action bar height to 0
            int actionBarHeight = 0;

            sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

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

            // Set up the checkbox listener for the SMS checkbox
            SwitchPreferenceCompat smsSwitch = findPreference("sms");
            assert smsSwitch != null;
            smsSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean isSelected = (Boolean) newValue;

                    // If the switch is selected, request the SMS permission otherwise disable it
                    if (isSelected && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestSMSPermission();
                    } else {
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
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("sms_number", phoneNumber);
                editor.apply();

                // Send a welcome sms message to the user
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, "Thank you for subscribing to weight tracking!", null, null);

                return true;
            });

            // Handle in-app messaging switch
            SwitchPreferenceCompat inAppMessagingSwitch = findPreference("in_app");
            assert inAppMessagingSwitch != null;

            // Create a Shared Preferences on change listener for in app messaging
            inAppMessagingSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                // If the switch is selected, enable in-app messaging otherwise disable it
                boolean isSelected = (Boolean) newValue;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isSelected) {
                    // Enable in-app messaging
                    editor.putBoolean("in_app_messaging", true);
                } else {
                    // Disable in-app messaging
                    editor.putBoolean("in_app_messaging", false);
                }
                editor.apply();
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
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("sms_enabled", true);
            editor.apply();

            // Show a toast message indicating that SMS notifications are enabled
            Toast.makeText(getContext(), "SMS Enabled", Toast.LENGTH_SHORT).show();
        }

        /***
         * Disable the SMS permission
         */
        private void disableSMSPermission() {
            // Save the SMS notification preference to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("sms_enabled", false);
            editor.apply();

            // Show a toast message indicating that SMS notifications are disabled
            Toast.makeText(getContext(), "SMS Disabled", Toast.LENGTH_SHORT).show();
        }
    }
}