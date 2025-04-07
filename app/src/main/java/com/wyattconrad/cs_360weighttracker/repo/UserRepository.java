package com.wyattconrad.cs_360weighttracker.repo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.wyattconrad.cs_360weighttracker.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDao userDao;
    private SharedPreferences sharedPreferences;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);


    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    //Login method
    public LiveData<User> login(String username, String password) {
        return userDao.login(username, password);
    }

    // Get User Id
    public LiveData<Long> getUserId(String username) {
        return userDao.getUserId(username);
    }

    // Get User First Name
    public LiveData<String> getUserFirstName(long userId) {
        return userDao.getUserFirstName(userId);
    }

    // Check if user exists
    public LiveData<Boolean> userExists(String username) {
        return userDao.userExists(username);
    }

    // Register New User
    public void registerUser(User user) {

        executorService.execute(() -> {
            long userId = userDao.insertUser(user);
            sharedPreferences.edit().putLong("user_id", userId).apply();
            user.setId(userId);
        });
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(userDao::deleteAll);
    }
}
