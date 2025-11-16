package com.wyattconrad.cs_360weighttracker.ui.registration;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wyattconrad.cs_360weighttracker.model.User;
import com.wyattconrad.cs_360weighttracker.repo.UserRepository;

public class RegistrationViewModel extends AndroidViewModel {

    // Declare variables
    private final UserRepository userRepository;

    // Constructor
    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Register new user
    public void registerUser(User user) {
        userRepository.registerUser(user);
    }

    // Login user
    public LiveData<User> login(String username, String password) {
        return userRepository.login(username, password);
    }

    // Check if username already exists
    public void checkForExistingUsername(String username, UserRepository.UsernameCallback callback) {
        userRepository.checkForExistingUsername(username, callback);
    }


}