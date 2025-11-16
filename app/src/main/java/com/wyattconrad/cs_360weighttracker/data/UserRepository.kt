package com.wyattconrad.cs_360weighttracker.repo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;

import com.wyattconrad.cs_360weighttracker.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDao userDao;
    private final SharedPreferences sharedPreferences;
    private final ExecutorService executorService;


    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        this.executorService = Executors.newFixedThreadPool(4);
        sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    // Constructor for Testing purposes
    @VisibleForTesting
    public UserRepository(UserDao userDao, ExecutorService executorService, SharedPreferences sharedPreferences){
        this.userDao = userDao;
        this.executorService = executorService;
        this.sharedPreferences = sharedPreferences;
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

    // Fetch the user
    public LiveData<User> fetchUser(long userId) {
        return userDao.fetchUser(userId);
    }

    public LiveData<String> getUsername(long userId) {
        return userDao.getUsername(userId);
    }



    // Register New User
    public void registerUser(User user) {

        executorService.execute(() -> {
            long userId = userDao.insertUser(user);
            sharedPreferences.edit().putLong("user_id", userId).apply();
            user.setId(userId);
        });
    }

    // Create an interface for the callback
    public interface UsernameCallback {
        void onUsernameExists(boolean exists);
    }

    // Check if username already exists
    public void checkForExistingUsername(String username, UsernameCallback callback) {
         executorService.execute(() -> {
             // Check if the username already exists in the database
             int count = userDao.countUsersByUsername(username);
             callback.onUsernameExists(count > 0);
         });
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(userDao::deleteAll);
    }
}
