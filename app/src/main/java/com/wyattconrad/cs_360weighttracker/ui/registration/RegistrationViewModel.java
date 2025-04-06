package com.wyattconrad.cs_360weighttracker.ui.registration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RegistrationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Please Register Your Account");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void login() {
        mText.setValue("Logged In");
    }

}