package com.wyattconrad.cs_360weighttracker.ui.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentRegistrationBinding;
import com.wyattconrad.cs_360weighttracker.model.User;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;
    private RegistrationViewModel registrationViewModel;
    private SharedPreferences sharedPreferences;
    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private boolean loggedIn = false;



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
        confirmPassword = binding.passwordConfirm;

        // Set the click listener for the register button
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        // Set the click listener for the login link
        binding.loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_login);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void register() {
        //TODO: Register user
        // Check if user already exists
        registrationViewModel.userExists(username.getText().toString()).observe(getViewLifecycleOwner(), userExists -> {
            if (userExists) {
                // User already exists
                binding.username.setError("Username already exists");
                return;
            }

            // Check if passwords match
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                binding.password.setError("Passwords do not match");
                return;
            }

            User newUser = new User(firstName.getText().toString(),
                    lastName.getText().toString(),
                    username.getText().toString(),
                    password.getText().toString());
            // Register user
            registrationViewModel.registerUser(newUser);

            // Login user
            registrationViewModel.login(username.getText().toString(), password.getText().toString()).observe(getViewLifecycleOwner(), user -> {
                if(user != null) {
                    // Save the user ID to SharedPreferences
                    sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("user_id", user.getId());
                    editor.putString("user_first_name", user.getFirstName());
                    editor.apply();
                    Log.d("LoginFragment", "User ID saved to SharedPreferences: " + user.getId());
                    Log.d("LoginFragment", "User First Name saved to SharedPreferences: " + user.getFirstName());

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
            });
        });
    }
}