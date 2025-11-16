package com.wyattconrad.cs_360weighttracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome Guest",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Your Goal Weight Is: 135.0 lbs",
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}