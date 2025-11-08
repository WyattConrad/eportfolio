package com.wyattconrad.cs_360weighttracker.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.wyattconrad.cs_360weighttracker.model.Goal;
import com.wyattconrad.cs_360weighttracker.model.User;

import org.jspecify.annotations.NonNull;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GoalRepositoryTest {

    // Setup mock dependencies
    @Mock
    GoalDao goalDao;

    @Mock
    UserDao userDao;

    @InjectMocks
    GoalRepository goalRepository;

    @InjectMocks
    UserRepository userRepository;


    private @NonNull Observer<? super Double> observer;


    @Test
    public void getGoalValue_for_existing_user_with_goal() {
        // Verify that getGoalValue returns a LiveData object containing the correct goal value for a user who already has a goal set in the database.
        
        User user = new User("John", "Smith", "johnsmith", "password");
        userRepository.registerUser(user);

        long userId = user.getId();



        Goal goal = new Goal(100.0, userId);
        
        when(goalDao.getGoalByUserId(userId)).thenReturn(goal);
        
        LiveData<Double> goalValueLiveData = goalRepository.getGoalValue(userId);

        assertNotNull(goalValueLiveData);
        goalValueLiveData.observeForever(observer);
        assertEquals(goal.getGoal(), goalValueLiveData.getValue(), 0.0);



    }

    @Test
    public void getGoalValue_for_user_without_goal() {
        // Verify that for a user ID that does not have an associated goal, a new goal with a default value of 0.0 is created and inserted into the database when the returned LiveData becomes active.
        // TODO implement test
    }

    @Test
    public void getGoalValue_with_invalid_user_ID() {
        // Test the behavior when a negative or zero user ID is passed. The system should handle this gracefully, likely by treating it as a user without a goal.
        // TODO implement test
    }

    @Test
    public void getGoalValue_LiveData_observation() {
        // Ensure that when the goal value is updated in the database for a user, the LiveData returned by getGoalValue emits the new value to its observers.
        // TODO implement test
    }

    @Test
    public void getGoalValue_database_error() {
        // Mock the GoalDao to throw an exception when getGoalValueByUserId is called and verify that the repository handles the error gracefully without crashing.
        // TODO implement test
    }

    @Test
    public void getGoalId_for_existing_user() {
        // Verify that getGoalId returns a LiveData object containing the correct goal ID for a user who has a goal in the database.
        // TODO implement test
    }

    @Test
    public void getGoalId_for_non_existent_user() {
        // For a user ID with no corresponding goal, verify that the returned LiveData object contains a null or empty value, and that no new goal is created.
        // TODO implement test
    }

    @Test
    public void getGoalId_with_invalid_user_ID() {
        // Pass a negative or zero user ID to getGoalId and verify that it returns a null or empty LiveData object without causing errors.
        // TODO implement test
    }

    @Test
    public void getGoalId_LiveData_updates() {
        // Although the goal ID is typically static, confirm that if the underlying data were to change (e.g., via a database migration), the LiveData would correctly notify its observers.
        // TODO implement test
    }

    @Test
    public void saveGoal_for_new_goal_insertion() {
        // Verify that calling saveGoal with a Goal object for a new user results in the insertGoal method of the GoalDao being called exactly once on a background thread.
        // TODO implement test
    }

    @Test
    public void saveGoal_for_existing_goal_update() {
        // Verify that calling saveGoal for a user who already has a goal results in the updateGoal method of the GoalDao being called.
        // Ensure the goal's value and timestamp are correctly updated.
        // TODO implement test
    }

    @Test
    public void saveGoal_with_null_goal_object() {
        // Test the behavior when a null Goal object is passed to saveGoal. The method should handle this without throwing a NullPointerException, likely by doing nothing.
        // TODO implement test
    }

    @Test
    public void saveGoal_with_extreme_goal_values() {
        // Test saving goals with values such as Double.MAX_VALUE, Double.MIN_VALUE, zero, and negative numbers to ensure they are handled correctly by the database.
        // TODO implement test
    }

    @Test
    public void saveGoal_thread_execution() {
        // Confirm that the database interaction within saveGoal (checking existence, inserting, or updating) occurs on a background thread managed by the executorService, not the main thread.
        // TODO implement test
    }

    @Test
    public void saveGoal_concurrent_calls() {
        // Test the scenario where saveGoal is called multiple times concurrently for the same userId to check for race conditions and ensure data integrity.
        // TODO implement test
    }

    @Test
    public void saveGoal_database_access_error() {
        // Mock the GoalDao to throw an exception during getGoalByUserId, insertGoal, or updateGoal calls and verify that the exception is caught and handled within the executorService thread, preventing an app crash.
        // TODO implement test
    }

}