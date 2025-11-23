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

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.wyattconrad.cs_360weighttracker.service.roundTo2
import org.junit.Rule
import org.junit.Test

class WeightToGoalCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // --- Test Case 1: Remaining Goal (weightToGoal > 0.0) ---
    @Test
    fun weightToGoalCard_displaysRemainingGoal_whenValueIsPositive() {
        // Arrange: Input value is positive (10.456 remaining)
        val remainingWeight = 10.456
        val expectedDisplayValue = remainingWeight.roundTo2().toString() // Expected: "10.46"

        // Act: Set the composable content
        composeTestRule.setContent {
            // Note: Replace with your actual package/path if needed
            WeightToGoalCard(weightToGoal = remainingWeight)
        }

        // Assert 1: Check the status text
        composeTestRule.onNodeWithText("Remaining Goal:")
            .assertIsDisplayed()

        // Assert 2: Check the rounded numeric value
        composeTestRule.onNodeWithText(expectedDisplayValue, substring = true)
            .assertIsDisplayed()

        // Assert 3: Check the unit text
        composeTestRule.onNodeWithText("lbs.")
            .assertIsDisplayed()
    }

    // --- Test Case 2: Goal Exactly Reached (weightToGoal == 0.0) ---
    @Test
    fun weightToGoalCard_displaysGoalReached_whenValueIsZero() {
        // Arrange: Input value is exactly zero (Goal reached)
        val zeroWeight = 0.0

        // Act: Set the composable content
        composeTestRule.setContent {
            WeightToGoalCard(weightToGoal = zeroWeight)
        }

        // Assert 1: Check the status text changes to "Goal Reached!"
        composeTestRule.onNodeWithText("Goal Reached!")
            .assertIsDisplayed()

        // Assert 2: Check the display value is fixed at "0.00"
        composeTestRule.onNodeWithText("0.0", substring = true)
            .assertIsDisplayed()
    }

    // --- Test Case 3: Goal Exceeded (weightToGoal < 0.0) ---
    @Test
    fun weightToGoalCard_displaysGoalReached_whenValueIsNegative() {
        // Arrange: Input value is negative (Goal exceeded)
        val exceededWeight = -3.75

        // Act: Set the composable content
        composeTestRule.setContent {
            WeightToGoalCard(weightToGoal = exceededWeight)
        }

        // Assert 1: Check the status text changes to "Goal Reached!"
        composeTestRule.onNodeWithText("Goal Reached!")
            .assertIsDisplayed()

        // Assert 2: Check the display value is fixed at "0.00" (Not the negative value)
        composeTestRule.onNodeWithText("0.0", substring = true)
            .assertIsDisplayed()
    }
}