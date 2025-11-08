package com.wyattconrad.cs_360weighttracker.repo;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;
import static org.awaitility.Awaitility.await;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.wyattconrad.cs_360weighttracker.model.User;

import org.jspecify.annotations.NonNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    // Setup mock dependencies
    @Mock
    UserDao userDao;

    @Mock
    SharedPreferences sharedPreferences;


    // This will NOT be automatically injected anymore, we will do it manually
    UserRepository userRepository;

    private @NonNull Observer<? super Double> observer;

    // Create an executor that runs tasks on the same thread.
    // This makes our asynchronous code synchronous for testing purposes.
    private final ExecutorService testExecutor = Executors.newSingleThreadExecutor();


    // ADD THIS RULE
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp() {
        // Manually create the UserRepository with the mocked Dao and the test executor
        userRepository = new UserRepository(userDao, testExecutor, sharedPreferences);
    }


    @Test
    public void login_with_valid_credentials() {
        // Verify that a user can log in successfully with the correct username and password. 
        // The returned LiveData<User> should emit a non-null User object.
        // TODO implement test
    }

    @Test
    public void login_with_incorrect_password() {
        // Test login with a valid username but an incorrect password. 
        // The returned LiveData<User> should emit a null value.
        // TODO implement test
    }

    @Test
    public void login_with_non_existent_username() {
        // Attempt to log in with a username that does not exist in the database. 
        // The returned LiveData<User> should emit a null value.
        // TODO implement test
    }

    @Test
    public void login_with_empty_credentials() {
        // Test login with an empty string for both username and password. 
        // The LiveData should emit null.
        // TODO implement test
    }

    @Test
    public void login_with_null_credentials() {
        // Test login with null values for username and password. 
        // This will likely test the underlying Dao's query handling for null parameters.
        // TODO implement test
    }

    @Test
    public void getUserId_for_existing_user() {
        // Verify that providing an existing username returns a LiveData object that emits the correct user ID (a Long value).
        // TODO implement test
    }

    @Test
    public void getUserId_for_non_existent_user() {
        // Test getting a user ID for a username that is not in the database. 
        // The returned LiveData should emit a null value.
        // TODO implement test
    }

    @Test
    public void getUserId_with_null_username() {
        // Call getUserId with a null username to check for null safety. 
        // The LiveData should emit null.
        // TODO implement test
    }

    @Test
    public void getUserFirstName_for_existing_user_ID() {
        // Verify that a valid and existing user ID returns a LiveData object that emits the correct first name string.
        // TODO implement test
    }

    @Test
    public void getUserFirstName_for_non_existent_user_ID() {
        // Test with a user ID (e.g., -1L or a large number) that does not correspond to any user. 
        // The returned LiveData should emit a null value.
        // TODO implement test
    }

    @Test
    public void userExists_for_an_existing_user() {
        // Check that for an existing username, the returned LiveData emits the boolean value 'true'.
        // TODO implement test
    }

    @Test
    public void userExists_for_a_non_existent_user() {
        // 1. Define the input for the test
        String nonExistentUsername = "NonExistentUser";

        // 2. Prepare the LiveData that the mock DAO will return
        MutableLiveData<Boolean> returnedLiveData = new MutableLiveData<>();
        // We are simulating that the database lookup found no user, so the LiveData value is 'false'
        returnedLiveData.setValue(false);

        // 3. Mock the DAO behavior: When userDao.userExists is called with our specific username,
        //    return the LiveData object we prepared.
        when(userDao.userExists(nonExistentUsername)).thenReturn(returnedLiveData);

        // 4. Create a mock Observer to capture the result from the LiveData
        @SuppressWarnings("unchecked") // Suppress warning for Observer casting
        Observer<Boolean> mockObserver = org.mockito.Mockito.mock(Observer.class);

        // 5. Call the actual method you are testing
        LiveData<Boolean> result = userRepository.userExists(nonExistentUsername);

        // 6. Attach the observer to the LiveData returned by the repository
        result.observeForever(mockObserver);

        // 7. Verify that the observer's onChanged method was called with the value 'false'.
        // This confirms that the LiveData correctly emitted the value from the DAO.
        verify(mockObserver).onChanged(false);
    }

    @Test
    public void userExists_with_case_sensitive_username() {
        // Test if 'Username' exists when 'username' is what's stored in the database to verify case sensitivity. 
        // The result should be 'false' assuming the query is case-sensitive.
        // TODO implement test
    }

    @Test
    public void register_a_new_unique_user() {
        // Test the successful registration of a new user with a unique username.
        // Verify that the userDao.insertUser method is called.

        // 1. Create a user object to be "registered"
        User newUser = new User("Mock", "User", "mockuser", "password");

        // 2. Call the method on the repository that you are testing
        userRepository.registerUser(newUser);

        // 3. Verify that the repository correctly called the insertUser method on the userDao.
        // This confirms that the registration logic is working as expected.
        // The `verify` method from Mockito checks if the interaction happened.
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() ->
                verify(userDao).insertUser(newUser));
    }


    @Test
    public void register_user_with_a_null_object() {
        // Attempt to register a null User object. 
        // This should be handled gracefully, likely throwing a NullPointerException or a similar error within the executor thread.
        // TODO implement test
    }

    @Test
    public void register_user_and_verify_thread_execution() {
        // Verify that the userDao.insertUser method is called on a background thread managed by executorService.
        // TODO implement test
    }

    @Test
    public void checkForExistingUsername_with_an_existing_username() {
        // Call the method with a username that is already in the database. 
        // Verify that the callback's onUsernameExists method is invoked with 'true'.
        // TODO implement test
    }

    @Test
    public void checkForExistingUsername_with_a_new_username() {
        // Call the method with a username that is not in the database. 
        // Verify that the callback's onUsernameExists method is invoked with 'false'.
        // TODO implement test
    }

    @Test
    public void checkForExistingUsername_callback_execution_on_background_thread() {
        // Verify that the database check and the callback invocation happen on the executorService's background thread.
        // TODO implement test
    }

    @Test
    public void deleteAll_with_a_populated_database() {
        // Add multiple users to the database and then call deleteAll(). 
        // Verify that the database is empty afterwards.
        // TODO implement test
    }

    @Test
    public void deleteAll_with_an_empty_database() {
        // Call deleteAll() on an already empty database. 
        // Verify that the operation completes without errors and the database remains empty.
        // TODO implement test
    }

}