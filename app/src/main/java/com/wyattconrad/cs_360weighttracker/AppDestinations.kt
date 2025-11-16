package com.wyattconrad.cs_360weighttracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings


sealed interface AppDestination {
    val icon: ImageVector
    val route: String
    val title: String
}

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
    override val title: String = "Log"
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

val appTabRowScreens = listOf(Home, Goal, Log, Login)