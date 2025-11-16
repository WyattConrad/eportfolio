package com.wyattconrad.cs_360weighttracker.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wyattconrad.cs_360weighttracker.model.User;
import com.wyattconrad.cs_360weighttracker.repo.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText;
    private final UserRepository userRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        mText = new MutableLiveData<>();
        mText.setValue("Please Login");
    }

    public LiveData<String> getText() {
        return mText;
    }


    public LiveData<User> login(String username, String password) {
        return userRepository.login(username, password);
    }

    public LiveData<Long> getUserId(String username) {
        return userRepository.getUserId(username);
    }

}