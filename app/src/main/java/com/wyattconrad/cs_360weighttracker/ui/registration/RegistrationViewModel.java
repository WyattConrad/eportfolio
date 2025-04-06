package com.wyattconrad.cs_360weighttracker.ui.registration;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.wyattconrad.cs_360weighttracker.repo.UserRepository;

public class RegistrationViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void register() {
        //TODO: Register user
    }

}