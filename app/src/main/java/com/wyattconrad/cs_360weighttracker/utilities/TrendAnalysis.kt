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
package com.wyattconrad.cs_360weighttracker.utilities

import com.wyattconrad.cs_360weighttracker.model.Weight
import java.time.ZoneOffset

/**
 * A utility class for performing linear regression on weight data.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
object TrendAnalysis {

    /**
     * Calculates the linear regression for a given list of weights.
     *
     * @param weights The list of weights to analyze.
     */
    fun calculateLinearRegression(weights: List<Weight>): RegressionResult {

        // Establish Constants
        val WEIGHTSTOEVALUATE = 100
        val MINWEIGHTSTOEVALUATE = 3

        // Initialize Variables
        var totalX = 0.0
        var totalY = 0.0
        var totalXYProduct = 0.0
        var totalXSquared = 0.0

        // Take the last 100 weights
        val filteredWeights = weights.takeLast(WEIGHTSTOEVALUATE)

        // Get all the date/time stamps of logged weights for the X axis
        val xValues = filteredWeights.map { it.dateTimeLogged.toEpochSecond(ZoneOffset.UTC) }
        // Get all the weight values for the Y axis
        val yValues = filteredWeights.map { it.weight }

        // Get teh count of weight values that have been logged (could be less than 100)
        val count = xValues.size

        // If there are less than 3 weight values, return 0.0 for slope and intercept
        // Not enough values for a proper trend analysis
        if (count < MINWEIGHTSTOEVALUATE) return RegressionResult(0.0, 0.0, emptyList())

        // Loop through the weight values to calculate the slope, intercept, and trend values
        for (i in 0 until count) {

            // Get the current weight and datetime values
            val x = xValues[i].toDouble()
            val y = yValues[i]

            // Sum the values for the slope and intercept calculations
            totalX += x
            totalY += y

            // Sum the products for the slope and intercept calculations
            totalXYProduct += x * y
            totalXSquared += x * x
        }

        // Calculate the slope and intercept using the sum of the products
        val slope = (count * totalXYProduct - totalX * totalY) /
                (count * totalXSquared - totalX * totalX)

        val intercept = (totalY - slope * totalX) / count

        // Place all the trend values in a list for plotting on the chart
        val trendValues : List<Double> = xValues.map { x ->
            slope * x + intercept
        }

        // Return the slope, intercept, and trend values
        return RegressionResult(slope, intercept, trendValues)
    }

    // Data class to hold the slope, intercept, and trend values
    data class RegressionResult(
        val slope: Double = 0.0,
        val intercept: Double = 0.0,
        val trendValues: List<Double> = emptyList()
    )
}
