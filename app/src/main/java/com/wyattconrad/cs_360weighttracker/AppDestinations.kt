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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings

/**
 * Sealed interface representing the different destinations in the app.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
sealed interface AppDestination {
    val icon: ImageVector
    val route: String
    val title: String
}

// Home, Goal, Log, Login, Settings destinations
data object Home : AppDestination {
    override val icon = Icons.Filled.Home
    override val route: String = "home"
    override val title: String = "Home"
}

data object Goal : AppDestination {
    override val icon = Icons.Filled.Check
    override val route: String = "goal"
    override val title: String = "Goal"
}

data object Log : AppDestination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val route: String = "log"
    override val title: String = "Weight Log"
}

data object Login : AppDestination {
    override val icon = Icons.Filled.Person
    override val route: String = "login"
    override val title: String = "Login"
}

data object Settings : AppDestination {
    override val icon = Icons.Filled.Settings
    override val route: String = "settings"
    override val title: String = "Settings"
}

// List of destinations that go on the bottom navigation bar
val appTabRowScreens = listOf(Home, Goal, Log, Login)