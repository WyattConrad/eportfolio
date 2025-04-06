package com.wyattconrad.cs_360weighttracker.repo;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.wyattconrad.cs_360weighttracker.model.Goal;
import com.wyattconrad.cs_360weighttracker.model.User;
import com.wyattconrad.cs_360weighttracker.model.Weight;
import java.time.LocalDateTime;
import java.util.List;
import android.util.Log;

public class WeightRepository {
    private final WeightDao weightDao;
    private final UserDao userDao;
    private final GoalDao goalDao;

    /***
     * Get the singleton instance of the repository
     * @param application The application context
     */
    public WeightRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        weightDao = db.weightDao();
        userDao = db.userDao();
        goalDao = db.goalDao();
    }

    public void addUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUser(user);
        });
    }

    public void addWeight(Weight weight) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            weightDao.insertWeight(weight);
        });
    }

    public void addGoal(Goal goal) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            goalDao.insertGoal(goal);
        });
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public LiveData<List<Weight>> getWeightByUserId(long userid) {
        return weightDao.getWeightByUserId(userid);
    }

    public LiveData<Weight> getWeightById(int id) {
        return weightDao.getWeightById(id);
    }

    public LiveData<Goal> getGoalByUserId(long userid) {
        return goalDao.getGoalByUserId(userid);
    }

    public void updateUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.updateUser(user);
        });
    }

    public void updateWeight(Weight weight) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            weightDao.updateWeight(weight);
        });
    }

    public void updateGoal(Goal goal) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            goalDao.updateGoal(goal);
        });
    }

    public void deleteUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.deleteUser(user);
        });
    }
    public void deleteWeight(Weight weight) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            weightDao.deleteWeight(weight);
        });
    }
    public void deleteGoal(Goal goal) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            goalDao.deleteGoal(goal);
        });
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            weightDao.deleteAll();
            goalDao.deleteAll();
        });
    }

}
