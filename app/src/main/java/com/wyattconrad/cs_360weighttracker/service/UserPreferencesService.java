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

public class UserPreferencesService {

    // Define Variables
    private final Context context;

    /**
     * Constructor for UserPreferencesService
     * @param context The application context
     */
    public UserPreferencesService(Context context) {
        this.context = context;
    }

    //Method to get the SharedPreferences file for each user id
    private String getPreferences(long userId) {
        return "user_prefs_" + userId;
    }

    //Method to get the SharedPreferences instance for a given user ID
    private SharedPreferences getUserPreferences(long userId) {
        return context.getSharedPreferences(getPreferences(userId), Context.MODE_PRIVATE);
    }

    //Method to save data for a specific user
    public void saveUserData(long userId, String key, String value) {
        SharedPreferences.Editor editor = getUserPreferences(userId).edit();
        editor.putString(key, value);
        editor.apply();
    }

    //Method to retrieve data for a specific user
    public String getUserData(long userId, String key, String defaultValue) {
        return getUserPreferences(userId).getString(key, defaultValue);
    }

    //Method to save boolean data for a specific user
    public void saveUserData(long userId, String key, boolean value) {
        SharedPreferences.Editor editor = getUserPreferences(userId).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    //Method to retrieve boolean data for a specific user
    public boolean getBoolean(long userId,String key, boolean defaultValue) {
        return getUserPreferences(userId).getBoolean(key, defaultValue);
    }

    //Method to clear all user data
    public void clearUserData(long userId){
        SharedPreferences.Editor editor = getUserPreferences(userId).edit();
        editor.clear();
        editor.apply();
    }

    //Method to delete the user preferences file.
    public boolean deleteUserPreferences(long userId){
        return context.deleteSharedPreferences(getPreferences(userId));
    }

}
