package com.wyattconrad.cs_360weighttracker.repo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

public class UserRepository {

    private final UserDao userDao;
    private SharedPreferences sharedPreferences;


    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    //Login method
    public LiveData<Boolean> login(String username, String password) {
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

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(userDao::deleteAll);
    }
}
