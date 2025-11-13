package com.wyattconrad.cs_360weighttracker.ui.log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogViewModel extends ViewModel {

    // Declare variables
    private final MutableLiveData<String> greetingText;


    // Constructor
    public LogViewModel() {
        greetingText = new MutableLiveData<>();
    }


}