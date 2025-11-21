/*
 * Copyright (C) 2025 Wyatt Conrad. All rights reserved.
 *
 * This file is part of the CS-360 Weight Tracker project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wyattconrad.cs_360weighttracker.ui.registration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentRegistrationBinding;
import com.wyattconrad.cs_360weighttracker.model.User;
import com.wyattconrad.cs_360weighttracker.service.LoginService;
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;
    private RegistrationViewModel registrationViewModel;
    private UserPreferencesService sharedPreferences;
    private LoginService loginService;
    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private Button registerBtn;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registrationViewModel =
                new ViewModelProvider(this).get(RegistrationViewModel.class);

        // Initialize the view binding
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the EditText fields
        firstName = binding.firstname;
        lastName = binding.lastname;
        username = binding.username;
        password = binding.password;
        registerBtn = binding.registerBtn;
        confirmPassword = binding.passwordConfirm;

        // Create a text changed listener for the username text entry to check if it already exists
        username.setOnFocusChangeListener((v, hasFocus) -> {
            // Check if the username already exists
            if (!hasFocus) {
                String usernameText = username.getText().toString();
                registrationViewModel.checkForExistingUsername(usernameText, usernameExists -> {
                    if (getActivity() != null) { // Check for null Activity
                        getActivity().runOnUiThread(() -> {
                            if (usernameExists) {
                                username.setError("Username already exists");
                                registerBtn.setEnabled(false);
                            } else {
                                username.setError(null);
                                registerBtn.setEnabled(true);
                            }
                        });
                    }
                });
            }
        });

        // Set the click listener for the register button
        binding.registerBtn.setOnClickListener(v -> register());

        // Set the click listener for the login link
        binding.loginLink.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_login);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void register() {

            // Check if passwords match
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                binding.password.setError("Passwords do not match");
                return;
            }

/*
            // Create new user
            User newUser = new User(firstName.getText().toString(),
                    lastName.getText().toString(),
                    username.getText().toString(),
                    password.getText().toString());

            // Register user
            registrationViewModel.registerUserCoroutine(newUser);
*/

            // Login user
            /*registrationViewModel.login(username.getText().toString(), password.getText().toString()).observe(getViewLifecycleOwner(), user -> {
                if(user != null) {

                    long userId = user.getId();

                    // Save the user ID to SharedPreferences
                    sharedPreferences = new UserPreferencesService(getContext());
                    loginService = new LoginService(requireContext());

                    // Save the user ID to SharedPreferences
                    loginService.saveUserId(userId);

                    // Save the user's first name and default notification preferences to SharedPreferences
                    sharedPreferences.saveUserData(userId, "user_first_name", user.getFirstName());
                    sharedPreferences.saveUserData(userId, "in_app_messaging", true);
                    sharedPreferences.saveUserData(userId, "sms_enabled", false);

                    // Log the user id and first name
                    Log.d("RegistrationFragment", "User ID" + userId);
                    Log.d("RegistrationFragment", "User First Name" + user.getFirstName());

                    // Change the bottom navigation login menu item to logout
                    BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
                    MenuItem loginItem = bottomNavigationView.getMenu().findItem(R.id.navigation_login);
                    MenuItem logoutItem = bottomNavigationView.getMenu().findItem(R.id.navigation_logout);
                    loginItem.setVisible(false);
                    logoutItem.setVisible(true);

                    // User logged in successfully, proceed to next screen or update UI
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.navigation_home);

                } else {

                    // Login failed, show an error message
                    Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    binding.password.setText("");
                    binding.password.requestFocus();
                    binding.password.setError("Invalid username or password");
                }

            });*/
    }
}