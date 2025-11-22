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
package com.wyattconrad.cs_360weighttracker.service

import com.wyattconrad.cs_360weighttracker.model.Weight
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * A service to help manage the Weights
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
class WeightService {
    // Method to calculate the weight to reach the goal
    fun calculateWeightToGoal(weights: MutableList<Weight>, goalValue: Double): Double {
        // Get the last weight from the list
        val last: Weight = weights.first()

        // Restrict the weight to goal to two decimal places
        val factor = 10.0.pow(2.0)

        // Calculate the weight to goal and return it
        return ((last.weight - goalValue) * factor).roundToInt() / factor
    }

    // Method to calculate the weight loss
    fun calculateWeightLoss(weights: MutableList<Weight>): Double {
        // Get the first and last weights from the list
        val last: Weight = weights.first()
        val first: Weight = weights.last()

        // Restrict the weight loss to two decimal places
        val factor = 10.0.pow(2.0)

        // Calculate the weight loss and return it
        return ((first.weight - last.weight) * factor).roundToInt() / factor
    }
}
