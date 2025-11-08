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
