package com.wyattconrad.cs_360weighttracker.ui.logout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wyattconrad.cs_360weighttracker.R;
import com.wyattconrad.cs_360weighttracker.databinding.FragmentLogoutBinding;
import com.wyattconrad.cs_360weighttracker.service.LoginService;

public class LogoutFragment extends Fragment {

    // Declare variables
    private FragmentLogoutBinding binding;
    private BottomNavigationView bottomNavigationView;
    private Button logoutBtn;
    private LoginService loginService;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        logoutBtn = binding.logoutBtn;

        bottomNavigationView = requireActivity().findViewById(R.id.nav_view);

        logoutBtn.setOnClickListener(v -> {
            // Clear the user ID from SharedPreferences
            loginService = new LoginService(requireContext());
            loginService.clearUserId();

            // Change the bottom navigation login menu item to logout
            MenuItem loginItem = bottomNavigationView.getMenu().findItem(R.id.navigation_login);
            MenuItem logoutItem = bottomNavigationView.getMenu().findItem(R.id.navigation_logout);
            loginItem.setVisible(true);
            logoutItem.setVisible(false);

            // User logged in successfully, proceed to next screen or update UI
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.navigation_login);
        });

        return root;
    }

}