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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wyattconrad.cs_360weighttracker.ui.components.AppTopBar
import com.wyattconrad.cs_360weighttracker.ui.components.NavRow
import com.wyattconrad.cs_360weighttracker.ui.theme.AppTheme
import com.wyattconrad.cs_360weighttracker.viewmodels.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity for the application.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                CS360WeightTrackerApp()
            }
        }
    }
}

// The main app composable function.
// Sets up the overall view for the app.
@Composable
fun CS360WeightTrackerApp(){
    // Sets up the navigation controller for the app.
    val navController = rememberNavController()
    // Sets up the current destination variables for the app.
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    val snackbarHostState = remember { SnackbarHostState() }

    val sessionViewModel: SessionViewModel = hiltViewModel()

    val currentScreen = appAllScreens.find { it.route == currentDestination?.route } ?: Home

    // Sets up the scaffold for the app.
    Scaffold(
        topBar = { AppTopBar(currentScreen, navController, sessionViewModel) },
        bottomBar = {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            ) {
                NavRow(
                    currentScreen = currentScreen,
                    navController = navController,
                    sessionViewModel = sessionViewModel
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}