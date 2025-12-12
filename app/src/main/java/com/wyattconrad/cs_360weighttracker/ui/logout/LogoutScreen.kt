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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.wyattconrad.cs_360weighttracker.Home
import com.wyattconrad.cs_360weighttracker.Login
import com.wyattconrad.cs_360weighttracker.R
import com.wyattconrad.cs_360weighttracker.ui.login.LoginViewModel
import com.wyattconrad.cs_360weighttracker.viewmodels.SessionViewModel

/**
 * Composable function for the logout screen.
 * @param navController The navigation controller.
 * @param logoutViewModel The logout view model.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Composable
fun LogoutScreen(
    navController: NavController,
    logoutViewModel: LogoutViewModel = hiltViewModel()
) {

    val sessionViewModel: SessionViewModel = hiltViewModel()

    // Column layout for the logout screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logout confirmation text
        Text(
            text = stringResource(R.string.confirm_logout),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Logout button
        Button(
            onClick = {
                logoutViewModel.logout()
                navController.navigate(Home.route)
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0075C4),
            ),
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = stringResource(R.string.logout))
        }
    }
}
