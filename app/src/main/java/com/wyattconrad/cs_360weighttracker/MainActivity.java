package com.wyattconrad.cs_360weighttracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.wyattconrad.cs_360weighttracker.databinding.ActivityMainBinding;
import com.wyattconrad.cs_360weighttracker.viewmodel.WeightListViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View navHost = findViewById(R.id.nav_host_fragment_activity_main);

        // Initialize the ViewModel
        WeightListViewModel mWeightListViewModel = new ViewModelProvider(this).get(WeightListViewModel.class);

        // Initialize the BottomNavigationView
        BottomNavigationView navView = binding.navView;

        // Get the user's ID from SharedPreferences, if one doesn't exist, set it to -1
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("user_id", -1);
        if (userId == -1) {
            // Navigate to the login page
            NavController navController = Navigation.findNavController(navHost);
            navController.navigate(R.id.navigation_login);
        }
        else {
            // Set the bottom navigation view to logout
            navView.getMenu().findItem(R.id.navigation_logout).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_login).setVisible(false);
        }



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_goal, R.id.navigation_login, R.id.navigation_logout)
                .build();

        // Initialize the NavController and set up the action bar
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the Up button in the action bar
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}