package com.wyattconrad.cs_360weighttracker.repo;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.wyattconrad.cs_360weighttracker.model.Goal;

public class GoalRepository {

    private LiveData<Double> goalWeight;
    private final GoalDao goalDao;

    public GoalRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        goalDao = db.goalDao();
    }

    public LiveData<Goal> getGoalWeight(long userId) {
        return goalDao.getGoalByUserId(userId);
    }

}
