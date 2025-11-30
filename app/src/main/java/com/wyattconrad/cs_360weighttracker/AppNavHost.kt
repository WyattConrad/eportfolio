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

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wyattconrad.cs_360weighttracker.ui.goal.GoalScreen
import com.wyattconrad.cs_360weighttracker.ui.home.HomeScreen
import com.wyattconrad.cs_360weighttracker.ui.home.HomeViewModel
import com.wyattconrad.cs_360weighttracker.ui.log.LogScreen
import com.wyattconrad.cs_360weighttracker.ui.login.LoginScreen
import com.wyattconrad.cs_360weighttracker.ui.logout.LogoutScreen
import com.wyattconrad.cs_360weighttracker.ui.registration.RegistrationScreen
import com.wyattconrad.cs_360weighttracker.ui.registration.RegistrationViewModel
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
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    // Create a navigation graph for the app.
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                events = viewModel.events
            )
        }
        composable(route = Goal.route) {
            GoalScreen()
        }
        composable(route = Log.route) {
            LogScreen()
        }
        composable(route = Login.route) {
            LoginScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(route = Logout.route) {
            LogoutScreen(
                navController = navController
            )
        }
        composable(route = Register.route) {
            // Get the ViewModel, scoped to the current composable lifecycle
            val viewModel: RegistrationViewModel = hiltViewModel()

            // Define the side-effect (Toast function) for the composable to use
            val context = LocalContext.current
            val showToast: (String) -> Unit = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            RegistrationScreen(
                onBackToLoginClick = {
                    // Navigate back to the Login destination.
                    navController.navigate(Login.route)
                },
                showToast = showToast,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = Settings.route) {

            val viewModel: SettingsViewModel = hiltViewModel()

            SettingsScreen(
                viewModel = viewModel,
                onPhoneComplete = {},
                events = viewModel.events,
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

