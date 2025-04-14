package com.wyattconrad.cs_360weighttracker.service;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginService {
    // Define constants
    private static final String PREF_NAME = "login_prefs";
    private static final String KEY_USER_ID = "user_id";

    // Define variables
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * Constructor for LoginService
     * @param context
     */
    public LoginService(Context context) {
        // Initialize variables
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Method to save the user ID
    public void saveUserId(long userId) {
        editor.putLong(KEY_USER_ID, userId);
        editor.apply();
    }

    // Method to get the logged in user Id
    public long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1);
    }

    // Method to clear the logged in user Id
    public void clearUserId() {
        editor.remove(KEY_USER_ID);
        editor.apply();
    }

    // Method to check if a user is logged in
    public boolean isLoggedIn() {
        return getUserId() != -1;
    }

}
