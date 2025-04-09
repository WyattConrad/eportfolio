package com.wyattconrad.cs_360weighttracker;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

            int actionBarHeight = 0;
            TypedValue tv = new TypedValue();
            if (requireActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }

            // Initialize permission launcher
            requestSMSPermissionLauncher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            Toast.makeText(getContext(), "SMS permission granted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "SMS permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            Log.d("ActionBarHeight", "ActionBar height: " + actionBarHeight);

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

                    if (isSelected && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestSMSPermission();
                    } else {
                        disableSMSPermission();
                    }
                    return true;
                });

            // Handle in-app messaging switch
            SwitchPreferenceCompat inAppMessagingSwitch = findPreference("in_app");
            assert inAppMessagingSwitch != null;

            inAppMessagingSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isSelected = (Boolean) newValue;
                if (isSelected) {
                    // Enable in-app messaging
                    sharedPreferences.edit().putBoolean("in_app_messaging", true).apply();
                } else {
                    // Disable in-app messaging
                    sharedPreferences.edit().putBoolean("in_app_messaging", false).apply();
                }
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
            sharedPreferences.edit().putBoolean("sms_enabled", true).apply();

            // Show a toast message indicating that SMS notifications are enabled
            Toast.makeText(getContext(), "SMS Enabled", Toast.LENGTH_SHORT).show();
        }

        /***
         * Disable the SMS permission
         */
        private void disableSMSPermission() {
            // Save the SMS notification preference to shared preferences
            sharedPreferences.edit().putBoolean("sms_enabled", false).apply();

            // Show a toast message indicating that SMS notifications are disabled
            Toast.makeText(getContext(), "SMS Disabled", Toast.LENGTH_SHORT).show();
        }
    }
}