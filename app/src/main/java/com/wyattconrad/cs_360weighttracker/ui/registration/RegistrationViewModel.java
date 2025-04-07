package com.wyattconrad.cs_360weighttracker.ui.registration;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wyattconrad.cs_360weighttracker.model.User;
import com.wyattconrad.cs_360weighttracker.repo.UserRepository;

public class RegistrationViewModel extends AndroidViewModel {

    private final UserRepository userRepository;


    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Check if user exists
    public LiveData<Boolean> userExists(String username) {
        return userRepository.userExists(username);
    }
    public void registerUser(User user) {
        userRepository.registerUser(user);
    }

    public LiveData<User> login(String username, String password) {
        return userRepository.login(username, password);
    }
    public void register() {
        //TODO: Register user
    }

}