package com.wyattconrad.cs_360weighttracker.service;

import com.wyattconrad.cs_360weighttracker.model.Weight;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class WeightServiceTest {

    @Test
    public void calculateWeightToGoal_with_a_null_weights_list() {
        // Test for NullPointerException when the input 'weights' list is null. [3, 9]

        ArrayList<Weight> weights = null;
        WeightService weightService = new WeightService();

        Assertions.assertThrows(NullPointerException.class, () -> {
            weightService.calculateWeightToGoal(weights, 100.0);
        });
    }

    @Test
    public void calculateWeightToGoal_with_an_empty_weights_list() {
        // Test for IndexOutOfBoundsException when the input 'weights' list is empty. [7, 8]
        ArrayList<Weight> weights = new ArrayList<>();

        WeightService weightService = new WeightService();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            weightService.calculateWeightToGoal(weights, 100.0);
        });

    }

    @Test
    public void calculateWeightToGoal_with_a_null_goalValue() {
        // Test for NullPointerException when the 'goalValue' is null, due to unboxing to a primitive double.

        ArrayList<Weight> weights = new ArrayList<>();

        WeightService weightService = new WeightService();

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            weightService.calculateWeightToGoal(weights, null);
        });
    }

    @Test
    public void calculateWeightToGoal_with_a_single_weight_entry() {
        // Test with a list containing a single Weight object to ensure it correctly retrieves the first and only element.

        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(100.0, -1);
        weights.add(weight);

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightToGoal(weights, 100.0);

        Assertions.assertEquals(0.0, result);

    }

    @Test
    public void calculateWeightToGoal_where_current_weight_is_greater_than_goal() {
        // Test with the latest weight being greater than the goal weight, expecting a positive result.
        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(100.0, -1);
        weights.add(weight);

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightToGoal(weights, 90.0);

        Assertions.assertEquals(10.0, result);
    }

    @Test
    public void calculateWeightToGoal_where_current_weight_is_less_than_goal() {
        // Test with the latest weight being less than the goal weight, expecting a negative result.
        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(100.0, -1);
        weights.add(weight);

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightToGoal(weights, 110.0);

        Assertions.assertEquals(-10.0, result);
    }

    @Test
    public void calculateWeightToGoal_where_current_weight_equals_goal() {
        // Test with the latest weight being equal to the goal weight, expecting a result of 0.0.
        ArrayList<Weight> weights = new ArrayList<>();
        LocalDateTime datetimelogged = LocalDateTime.now();
        Weight weight = new Weight(100.0, -1, datetimelogged);
        weights.add(weight);

        LocalDateTime datetimelogged2 = LocalDateTime.now().plusHours(1);
        Weight weight2 = new Weight(90.0, -1, datetimelogged2);
        weights.add(weight2);

        // Order the weights by datetime logged
        weights.sort((w1, w2) -> Long.compare(w2.getDateTimeLogged(), w1.getDateTimeLogged()));


        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightToGoal(weights, 90.0);

        Assertions.assertEquals(0.0, result);
    }

    @Test
    public void calculateWeightToGoal_with_large_weight_and_goal_values() {
        // Test with large double values for weight and goal to check for precision issues or overflow.
        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(999.0, -1);
        weights.add(weight);

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightToGoal(weights, 1.0);

        Assertions.assertEquals(998.0, result);
    }

    @Test
    public void calculateWeightToGoal_with_floating_point_precision_check() {
        // Test the rounding logic by using input values that result in more than two decimal places before rounding. [2, 18]

        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(100.123456789, -1);
        weights.add(weight);

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightToGoal(weights, 99.123456789);

        Assertions.assertEquals(1.000000000, result);

    }

    @Test
    public void calculateWeightToGoal_with_multiple_weight_entries() {
        // Test with a list of multiple weights to confirm the method incorrectly uses weights.get(0) instead of the latest weight.
        ArrayList<Weight> weights = new ArrayList<>();
        LocalDateTime datetimelogged = LocalDateTime.now().minusDays(10);
        long currentWeight = 100L;

        for (int i = 0; i <= 10; i++) {

            Weight weight = new Weight(currentWeight, -1, datetimelogged);
            weights.add(weight);

            datetimelogged = datetimelogged.plusDays(1);
            currentWeight--;
        }

        // Order the weights by datetime logged
        weights.sort(Comparator.comparing(Weight::getDateTimeLogged).reversed());

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightToGoal(weights, 90.0);

        Assertions.assertEquals(0.0, result);
    }

    @Test
    public void calculateWeightLoss_with_a_null_weights_list() {
        // Test for NullPointerException when the input 'weights' list is null. [3, 9]
        ArrayList<Weight> weights = null;
        WeightService weightService = new WeightService();

        Assertions.assertThrows(NullPointerException.class, () -> {
            weightService.calculateWeightLoss(weights);
        });
    }

    @Test
    public void calculateWeightLoss_with_an_empty_weights_list() {
        // Test for IndexOutOfBoundsException as the code attempts to access elements from an empty list. [7, 8]
        ArrayList<Weight> weights = new ArrayList<>();
        WeightService weightService = new WeightService();

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            weightService.calculateWeightLoss(weights);
        });
    }

    @Test
    public void calculateWeightLoss_with_a_single_weight_entry() {
        // Test with a list containing only one Weight object, which should result in an IndexOutOfBoundsException due to weights.get(weights.size() - 1) logic if not handled.
        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(100.0, -1);
        weights.add(weight);

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightLoss(weights);

        Assertions.assertEquals(0.0, result);
    }

    @Test
    public void calculateWeightLoss_with_two_weight_entries_showing_weight_loss() {
        // Test with a list of two weights where the first weight is greater than the last, expecting a positive result. 
        // NOTE: The implementation has a bug, it uses get(0) as last and get(size-1) as first.

        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(100.0, -1);
        weights.add(weight);

        Weight weight2 = new Weight(90.0, -1);
        weights.add(weight2);


        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightLoss(weights);

        Assertions.assertEquals(-10.0, result);
    }

    @Test
    public void calculateWeightLoss_with_two_weight_entries_showing_weight_gain() {
        // Test with a list of two weights where the first weight is less than the last, expecting a negative result. 

        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(100.0, -1);
        weights.add(weight);

        Weight weight2 = new Weight(110.0, -1);
        weights.add(weight2);


        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightLoss(weights);

        Assertions.assertEquals(10.0, result);
    }

    @Test
    public void calculateWeightLoss_with_multiple_weight_entries() {
        // Test with a list containing multiple weights to verify it correctly calculates the difference between the first and last recorded weights. 

        ArrayList<Weight> weights = new ArrayList<>();
        LocalDateTime datetimelogged = LocalDateTime.now().minusDays(10);
        long currentWeight = 100L;

        for (int i = 0; i <= 10; i++) {

            Weight weight = new Weight(currentWeight, -1, datetimelogged);
            weights.add(weight);

            datetimelogged = datetimelogged.plusDays(1);
            currentWeight--;
        }

        // Order the weights by datetime logged
        weights.sort(Comparator.comparing(Weight::getDateTimeLogged).reversed());

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightLoss(weights);

        Assertions.assertEquals(10.0, result);
    }

    @Test
    public void calculateWeightLoss_where_first_and_last_weights_are_equal() {
        // Test with a list where the first and last weights are identical, expecting a result of 0.0.
        ArrayList<Weight> weights = new ArrayList<>();
        LocalDateTime datetimelogged = LocalDateTime.now().minusDays(10);
        long currentWeight = 100L;

        for (int i = 0; i <= 10; i++) {

            Weight weight = new Weight(currentWeight, -1, datetimelogged);
            weights.add(weight);

            datetimelogged = datetimelogged.plusDays(1);
            currentWeight--;
        }

        Weight weight = new Weight(100.0, -1, datetimelogged);
        weights.add(weight);


        // Order the weights by datetime logged
        weights.sort(Comparator.comparing(Weight::getDateTimeLogged).reversed());

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightLoss(weights);

        Assertions.assertEquals(0.0, result);
    }

    @Test
    public void calculateWeightLoss_with_large_weight_values() {
        // Test with large double values for weights to check for potential floating-point precision issues or overflow.
        ArrayList<Weight> weights = new ArrayList<>();
        LocalDateTime datetimelogged = LocalDateTime.now().minusDays(10);
        long currentWeight = 999L;

        for (int i = 0; i <= 8; i++) {

            Weight weight = new Weight(currentWeight, -1, datetimelogged);
            weights.add(weight);

            datetimelogged = datetimelogged.plusDays(1);
            currentWeight -= 100;
        }

        // Order the weights by datetime logged
        weights.sort(Comparator.comparing(Weight::getDateTimeLogged).reversed());

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightLoss(weights);

        Assertions.assertEquals(800.0, result);
    }

    @Test
    public void calculateWeightLoss_with_floating_point_precision_check() {
        // Test the rounding logic by using weight values that result in a difference with more than two decimal places before rounding. [2, 18]
        ArrayList<Weight> weights = new ArrayList<>();
        LocalDateTime datetimelogged = LocalDateTime.now().minusDays(10);
        double currentWeight = (double) 100.123456789;
        double decrementAmount = (double) 2.123456789;


        for (int i = 0; i <10; i++) {

            System.out.println("Current Weight: " + String.valueOf(currentWeight));
            Weight weight = new Weight(currentWeight, -1, datetimelogged);
            weights.add(weight);

            datetimelogged = datetimelogged.plusDays(1);
            currentWeight = currentWeight - decrementAmount;
        }

        // Order the weights by datetime logged
        weights.sort(Comparator.comparing(Weight::getDateTimeLogged).reversed());

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightLoss(weights);

        Assertions.assertEquals(19.11, result);
    }

    @Test
    public void calculateWeightLoss_with_a_list_containing_a_null_Weight_object() {
        // Test how the method handles a list that contains a null Weight object, which may lead to a NullPointerException when getWeight() is called. [3, 9]
        ArrayList<Weight> weights = new ArrayList<>();
        Weight weight = new Weight(100.0, -1);
        weights.add(weight);

        Weight weight2 = null;
        weights.add(weight2);

        Weight weight3 = new Weight(110.0, -1);
        weights.add(weight3);

        WeightService weightService = new WeightService();
        double result = weightService.calculateWeightLoss(weights);

        Assertions.assertEquals(10.0, result);
    }

}