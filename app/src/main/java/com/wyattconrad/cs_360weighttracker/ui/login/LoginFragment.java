package com.wyattconrad.cs_360weighttracker.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        loginViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();
                loginViewModel.login(username, password).observe(getViewLifecycleOwner(), user -> {
                    if(user != null) {
                        // Save the user ID to SharedPreferences
                        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("user_id", user.getId());
                        editor.putString("user_first_name", user.getFirstName());
                        editor.apply();

                        // Get the values just saved to SharedPreferences
                        long userIdFromPrefs = sharedPreferences.getLong("user_id", -1);
                        String userFirstNameFromPrefs = sharedPreferences.getString("user_first_name", "");

                        Log.d("LoginFragment", "User ID saved to SharedPreferences: " + userIdFromPrefs);
                        Log.d("LoginFragment", "User First Name saved to SharedPreferences: " + userFirstNameFromPrefs);

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
            }
        });
        binding.registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.navigation_registration);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}