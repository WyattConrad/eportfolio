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
package com.wyattconrad.cs_360weighttracker.service

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * A service to help manage the user preferences
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
class UserPreferencesService
/**
 * Constructor for UserPreferencesService
 * @param context The application context
 */(// Define Variables
    private val context: Context
) {
    //Method to get the SharedPreferences file for each user id
    private fun getPreferences(userId: Long): String {
        return "user_prefs_$userId"
    }

    //Method to get the SharedPreferences instance for a given user ID
    private fun getUserPreferences(userId: Long): SharedPreferences? {
        return context.getSharedPreferences(getPreferences(userId), Context.MODE_PRIVATE)
    }

    //Method to save data for a specific user
    fun saveUserData(userId: Long, key: String?, value: String?) {
        getUserPreferences(userId)!!.edit {
            putString(key, value)
        }
    }

    //Method to retrieve data for a specific user
    fun getUserData(userId: Long, key: String?, defaultValue: String?): String? {
        return getUserPreferences(userId)!!.getString(key, defaultValue)
    }

    //Method to save boolean data for a specific user
    fun saveUserData(userId: Long, key: String?, value: Boolean) {
        getUserPreferences(userId)!!.edit {
            putBoolean(key, value)
        }
    }

    fun getString(userId: Long, key: String?, defaultValue: String?): String? {
        return getUserPreferences(userId)!!.getString(key, defaultValue)
    }

    fun putString(userId: Long, key: String?, value: String?) {
        getUserPreferences(userId)!!.edit { putString(key, value) }
    }

    fun putBoolean(userId: Long, key: String?, value: Boolean) {
        getUserPreferences(userId)!!.edit { putBoolean(key, value) }
    }

    //Method to retrieve boolean data for a specific user
    fun getBoolean(userId: Long, key: String?, defaultValue: Boolean): Boolean {
        return getUserPreferences(userId)!!.getBoolean(key, defaultValue)
    }

    //Method to clear all user data
    fun clearUserData(userId: Long) {
        getUserPreferences(userId)!!.edit {
            clear()
        }
    }

    //Method to delete the user preferences file.
    fun deleteUserPreferences(userId: Long): Boolean {
        return context.deleteSharedPreferences(getPreferences(userId))
    }
}
