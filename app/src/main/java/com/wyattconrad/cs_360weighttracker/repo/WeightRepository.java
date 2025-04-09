package com.wyattconrad.cs_360weighttracker.repo;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.wyattconrad.cs_360weighttracker.model.Weight;
import java.util.List;

public class WeightRepository {
    // Declare variables
    private final WeightDao weightDao;

    /***
     * Get the singleton instance of the repository
     * @param application The application context
     */
    public WeightRepository(Application application) {
        // Initialize the database and weightDao
        AppDatabase db = AppDatabase.getDatabase(application);
        weightDao = db.weightDao();
    }

    public void addWeight(Weight weight) {
        // Insert the weight into the database on a background thread
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Insert the weight into the database and get the Id
            long weightId = weightDao.insertWeight(weight);
            // Set the Id of the weight to the Id returned from the database
            weight.setId(weightId);
        });
    }

    public LiveData<List<Weight>> getWeightByUserId(long userid) {
        // Get the weight from the database and return it as a LiveData object
        return weightDao.getWeightByUserId(userid);
    }

    public LiveData<Weight> getFirstWeightByUserId(long userId) {
        // Get the first weight from the database and return it as a LiveData object
        return weightDao.getFirstWeightByUserId(userId);
    }

    public LiveData<Weight> getLastWeightByUserId(long userId) {
        // Get the last weight from the database and return it as a LiveData object
        return weightDao.getLastWeightByUserId(userId);
    }

    public void updateWeight(Weight weight) {
        // Update the weight in the database on a background thread
        AppDatabase.databaseWriteExecutor.execute(() -> {
            weightDao.updateWeight(weight);
        });
    }

    public void deleteWeight(Weight weight) {
        // Delete the weight from the database on a background thread
        AppDatabase.databaseWriteExecutor.execute(() -> {
            weightDao.deleteWeight(weight);
        });
    }

}
