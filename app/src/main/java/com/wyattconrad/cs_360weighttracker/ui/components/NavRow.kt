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

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.wyattconrad.cs_360weighttracker.AppDestination
import com.wyattconrad.cs_360weighttracker.appTabRowScreens
import com.wyattconrad.cs_360weighttracker.navigateSingleTopTo

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
    navController: NavHostController
) {
    // The navigation bar to be displayed
    NavigationBar {
        // Loop through the navigation items and add them to the navigation bar
        appTabRowScreens.forEach { screen ->
            NavigationBarItem(
                selected = screen.route == currentScreen.route,
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
    }
}