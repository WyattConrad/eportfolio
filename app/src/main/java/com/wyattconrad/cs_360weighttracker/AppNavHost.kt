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
package com.wyattconrad.cs_360weighttracker

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wyattconrad.cs_360weighttracker.ui.goal.GoalScreen
import com.wyattconrad.cs_360weighttracker.ui.home.HomeScreen
import com.wyattconrad.cs_360weighttracker.ui.log.LogScreen
import com.wyattconrad.cs_360weighttracker.ui.login.LoginScreen
import com.wyattconrad.cs_360weighttracker.ui.settings.SettingsScreen
import com.wyattconrad.cs_360weighttracker.ui.settings.SettingsViewModel

/**
 * AppNavHost is the navigation host for the app.
 * @param navController The navigation controller.
 * @param modifier The modifier to apply to the navigation host.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Create a navigation graph for the app.
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen()
        }
        composable(route = Goal.route) {
            GoalScreen()
        }
        composable(route = Log.route) {
            LogScreen()
        }
        composable(route = Login.route) {
            LoginScreen()
        }
        composable(route = Settings.route) {

            val context = LocalContext.current
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            // Permission launcher
            val smsPermissionLauncher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { granted ->
                    viewModel.onSmsPermissionResult(granted)
                }

            SettingsScreen(
                uiState = uiState,
                onSmsToggle = { enabled ->
                    viewModel.toggleSms(enabled)

                    if (enabled) {
                        smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                    } else {
                        viewModel.onSmsPermissionResult(false)
                    }
                },
                onPhoneChange = viewModel::updatePhoneNumber,
                onInAppToggle = viewModel::toggleInApp,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// Helper function to navigate to a destination.
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

