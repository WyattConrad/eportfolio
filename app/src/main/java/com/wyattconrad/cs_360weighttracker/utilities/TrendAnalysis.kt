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

object TrendAnalysis {

    fun calculateLinearRegression(weights: List<Weight>): RegressionResult {

        val filteredWeights = weights.takeLast(100)


        val epochStart = filteredWeights.first().dateTimeLogged.toEpochSecond(ZoneOffset.UTC)
        val xValues = filteredWeights.map { it.dateTimeLogged.toEpochSecond(ZoneOffset.UTC) }
        val yValues = filteredWeights.map { it.weight }

        val count = xValues.size

        if (count < 3) return RegressionResult(0.0, 0.0, emptyList())

        var totalX = 0.0
        var totalY = 0.0
        var totalXYProduct = 0.0
        var totalXSquared = 0.0

        for (i in 0 until count) {
            val x = xValues[i].toDouble()
            val y = yValues[i]

            totalX += x
            totalY += y
            totalXYProduct += x * y
            totalXSquared += x * x
        }

        val slope = (count * totalXYProduct - totalX * totalY) /
                (count * totalXSquared - totalX * totalX)

        val intercept = (totalY - slope * totalX) / count

        val trendValues : List<Double> = xValues.map { x ->
            slope * x + intercept
        }

        return RegressionResult(slope, intercept, trendValues)
    }

    data class RegressionResult(
        val slope: Double,
        val intercept: Double,
        val trendValues: List<Double>
    )
}
