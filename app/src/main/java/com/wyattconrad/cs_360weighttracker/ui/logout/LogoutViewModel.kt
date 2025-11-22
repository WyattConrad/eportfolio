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
package com.wyattconrad.cs_360weighttracker.ui.logout

import androidx.lifecycle.ViewModel
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import com.wyattconrad.cs_360weighttracker.viewmodels.SessionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * LogoutViewModel for the Logout Screen
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val prefs: UserPreferencesService
) : ViewModel() {

    // Clear the user session
    fun logout() {
        prefs.putGlobalLong("userId", -1L)
    }

}