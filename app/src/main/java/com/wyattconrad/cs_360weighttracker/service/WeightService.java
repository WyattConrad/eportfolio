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
package com.wyattconrad.cs_360weighttracker.service;

import com.wyattconrad.cs_360weighttracker.model.Weight;

import java.util.List;

public class WeightService {

    public double calculateWeightToGoal(List<Weight> weights, Double goalValue) {
        // Get the last weight from the list
        Weight last = weights.get(0);

        // Calculate the weight left to reach the goal
        double factor = Math.pow(10, 2);

        return Math.round((last.getWeight() - goalValue) * factor) / factor;
    }

    public double calculateWeightLoss(List<Weight> weights) {
        // Get the first and last weights from the list
        Weight last = weights.get(0);
        Weight first = weights.get(weights.size() - 1);

        double factor = Math.pow(10, 2);

        return Math.round((first.getWeight() - last.getWeight()) * factor) / factor;
    }
}
