package com.wyattconrad.cs_360weighttracker.ui.addweight;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.wyattconrad.cs_360weighttracker.model.Weight;
import com.wyattconrad.cs_360weighttracker.repo.WeightRepository;

public class AddWeightViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private final WeightRepository weightRepository;

    public AddWeightViewModel(Application application) {
        super(application);
        weightRepository = new WeightRepository(application);
    }

    public void addWeight(Weight weight) {
        weightRepository.addWeight(weight);
    }
}