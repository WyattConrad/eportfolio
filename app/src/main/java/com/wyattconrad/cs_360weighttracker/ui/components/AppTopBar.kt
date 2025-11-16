package com.wyattconrad.cs_360weighttracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.wyattconrad.cs_360weighttracker.AppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(currentScreen: AppDestination) {
    TopAppBar(
        title = {
            Text(
                text = currentScreen.title,  // use whatever text you want
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = { /* settings click */ }) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}