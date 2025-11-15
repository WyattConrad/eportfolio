package com.wyattconrad.cs_360weighttracker.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wyattconrad.cs_360weighttracker.model.Weight;
import com.wyattconrad.cs_360weighttracker.repo.WeightRepository;
import java.util.List;

public class WeightListViewModel extends AndroidViewModel {
    private final WeightRepository weightRepository;

    public WeightListViewModel(Application application) {
        super(application);
        weightRepository = new WeightRepository(application);
    }

    public LiveData<List<Weight>> getWeightByUserId(long id) {
        return weightRepository.getWeightByUserId(id);
    }

    public void addWeight(Weight weight) {
        weightRepository.addWeight(weight);
    }

    public void updateWeight(Weight weight) {
        weightRepository.updateWeight(weight);
    }

    public void deleteWeight(Weight weight) {
        weightRepository.deleteWeight(weight);
    }

    public LiveData<Double> getWeightLostByUserId(long id) {
        return weightRepository.getWeightLostByUserId(id);
    }

    public LiveData<Double> getWeightToGoalByUserId(long userId){
        return weightRepository.getWeightToGoalByUserId(userId);

    }
}
