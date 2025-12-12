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

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.wyattconrad.cs_360weighttracker.service.roundTo2
import org.junit.Rule
import org.junit.Test

class WeightChangeCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test Case: Weight Lost (change is positive)
    @Test
    fun weightChangeCard_displaysWeightLost_whenChangeIsPositive() {
        val positiveChange = 5.342

        // Set the content with the component under test
        composeTestRule.setContent {
            WeightChangeCard(weightChange = positiveChange)
        }

        // Assert the correct status text is displayed
        composeTestRule.onNodeWithText("Weight Lost:")
            .assertIsDisplayed()

        // Assert the correct absolute and rounded value is displayed
        val expectedValue = positiveChange.roundTo2().toString()
        composeTestRule.onNodeWithText(expectedValue, substring = true)
            .assertIsDisplayed()

        // Assert the unit is displayed
        composeTestRule.onNodeWithText("lbs.")
            .assertIsDisplayed()

    }

    // Test Case: Weight Gained (change is negative)
    @Test
    fun weightChangeCard_displaysWeightGained_whenChangeIsNegative() {
        val negativeChange = -2.759

        composeTestRule.setContent {
            WeightChangeCard(weightChange = negativeChange)
        }

        // Assert the correct status text is displayed
        composeTestRule.onNodeWithText("Weight Gained:")
            .assertIsDisplayed()

        // Assert the absolute, rounded value is displayed
        val expectedAbsoluteValue = kotlin.math.abs(negativeChange).roundTo2().toString()
        composeTestRule.onNodeWithText(expectedAbsoluteValue, substring = true)
            .assertIsDisplayed()

    }

    // Test Case: Net Change (change == 0.0)
    @Test
    fun weightChangeCard_displaysNetChange_whenChangeIsZero() {
        val zeroChange = 0.0

        composeTestRule.setContent {
            WeightChangeCard(weightChange = zeroChange)
        }

        // Assert the correct status text is displayed
        composeTestRule.onNodeWithText("Net Change:")
            .assertIsDisplayed()

        // Assert the zero value is displayed
        composeTestRule.onNodeWithText("0.0")
            .assertIsDisplayed()

    }
}