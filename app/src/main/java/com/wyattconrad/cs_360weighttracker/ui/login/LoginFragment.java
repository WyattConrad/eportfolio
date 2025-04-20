package com.wyattconrad.cs_360weighttracker.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentLoginBinding;
import com.wyattconrad.cs_360weighttracker.service.LoginService;
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserPreferencesService sharedPreferences;
    private LoginService loginService;
    private BottomNavigationView bottomNavigationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bottomNavigationView = requireActivity().findViewById(R.id.nav_view);

        final TextView textView = binding.textHeader;
        loginViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.loginBtn.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            loginViewModel.login(username, password).observe(getViewLifecycleOwner(), user -> {
                if(user != null) {
                    // Save the user ID to SharedPreferences
                    long userId = user.getId();
                    loginService = new LoginService(requireContext());
                    loginService.saveUserId(userId);
                    sharedPreferences = new UserPreferencesService(getContext());
                    sharedPreferences.saveUserData(userId, "user_first_name", user.getFirstName());

                    // Get the values just saved to SharedPreferences
                    long userIdFromPrefs = loginService.getUserId();
                    String userFirstNameFromPrefs = sharedPreferences.getUserData(userId, "user_first_name", "Guest");

                    Log.d("LoginFragment", "User ID saved to SharedPreferences: " + userIdFromPrefs);
                    Log.d("LoginFragment", "User First Name saved to SharedPreferences: " + userFirstNameFromPrefs);

                    // Change the bottom navigation login menu item to logout
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
            });
        });
        binding.registerLink.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_registration);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}