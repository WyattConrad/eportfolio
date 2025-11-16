/*
 * Copyright (C) 2025 Wyatt Conrad. All rights reserved.
 *
 * This file is part of the CS-360 Weight Tracker project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wyattconrad.cs_360weighttracker.service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * A service to help manage user login.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
public class LoginService {

    // Define constants
    private static final String PREF_NAME = "login_prefs";
    private static final String KEY_USER_ID = "user_id";

    // Define variables
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    /**
     * Constructor for LoginService
     * @param context The application context
     */
    public LoginService(Context context) {
        // Initialize variables
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
        return sharedPreferences.getLong(KEY_USER_ID, 1);
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
