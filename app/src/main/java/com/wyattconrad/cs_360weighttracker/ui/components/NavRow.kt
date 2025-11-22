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
package com.wyattconrad.cs_360weighttracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wyattconrad.cs_360weighttracker.AppDestination
import com.wyattconrad.cs_360weighttracker.Login
import com.wyattconrad.cs_360weighttracker.Logout
import com.wyattconrad.cs_360weighttracker.appTabRowScreens
import com.wyattconrad.cs_360weighttracker.navigateSingleTopTo
import com.wyattconrad.cs_360weighttracker.viewmodels.SessionViewModel

/**
 * Navigation Row Composable for the app
 * @param currentScreen The current screen
 * @param navController The navigation controller
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun NavRow(
    currentScreen: AppDestination,
    navController: NavHostController,
    sessionViewModel: SessionViewModel
) {

    val loggedInUserId = sessionViewModel.loggedInUserId.collectAsState()

    val isLoggedIn = loggedInUserId.value != -1L

    // Get the current back stack entry (which is nullable)
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()

    // Dynamic Login/Logout Item
    val authItemLabel = if (isLoggedIn) "Logout" else "Login"
    val authIcon = if (isLoggedIn) Icons.AutoMirrored.Filled.ExitToApp else Icons.Filled.Person
    val currentRoute = currentNavBackStackEntry?.destination?.route

    // Check if the user is logged in and set routing accordingly
    val isAuthItemSelected = currentRoute == Login.route || currentRoute == Logout.route

    // The navigation bar to be displayed
    NavigationBar {
        // Loop through the navigation items and add them to the navigation bar
        appTabRowScreens.forEach { screen ->
            NavigationBarItem(
                selected = !isAuthItemSelected && screen.route == currentScreen.route,
                onClick = { navController.navigateSingleTopTo(screen.route) },
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.route
                    )
                },
                label = { Text(screen.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,    // icon color when selected
                    unselectedIconColor = Color.Gray,     // icon color when not selected
                    indicatorColor = Color(0xFF0075C4)   // background highlight circle
                )
            )
        }

        NavigationBarItem(
            selected = isAuthItemSelected,
            onClick = {
                if (isLoggedIn) {
                    sessionViewModel.clearSession()
                    // Navigate to Home or Login screen after logout
                    navController.navigateSingleTopTo(Logout.route)
                } else {
                    navController.navigateSingleTopTo(Login.route)
                }
            },
            icon = {
                Icon(imageVector = authIcon, contentDescription = authItemLabel)
            },
            label = { Text(authItemLabel) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,    // icon color when selected
                unselectedIconColor = Color.Gray,     // icon color when not selected
                indicatorColor = Color(0xFF0075C4)   // background highlight circle
            )
        )

    }
}