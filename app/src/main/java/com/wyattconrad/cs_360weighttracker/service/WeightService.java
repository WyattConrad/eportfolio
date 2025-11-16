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

/**
 * A service to help manage the Weights
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
public class WeightService {

    // Method to calculate the weight to reach the goal
    public double calculateWeightToGoal(List<Weight> weights, Double goalValue) {
        // Get the last weight from the list
        Weight last = weights.getFirst();

        // Restrict the weight to goal to two decimal places
        double factor = Math.pow(10, 2);

        // Calculate the weight to goal and return it
        return Math.round((last.getWeight() - goalValue) * factor) / factor;
    }

    // Method to calculate the weight loss
    public double calculateWeightLoss(List<Weight> weights) {
        // Get the first and last weights from the list
        Weight last = weights.getFirst();
        Weight first = weights.getLast();

        // Restrict the weight loss to two decimal places
        double factor = Math.pow(10, 2);

        // Calculate the weight loss and return it
        return Math.round((first.getWeight() - last.getWeight()) * factor) / factor;
    }
}
