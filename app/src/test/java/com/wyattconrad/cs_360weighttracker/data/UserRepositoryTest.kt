package com.wyattconrad.cs_360weighttracker.data

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wyattconrad.cs_360weighttracker.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.awaitility.Awaitility
import org.awaitility.core.ThrowingRunnable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class UserRepositoryTest {
    // Setup mock dependencies
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository

    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        userDao = mockk()
        sharedPreferences = mockk()

        userRepository = UserRepository(userDao)

    }


    @Test
    fun login_with_valid_credentials() {
        // Verify that a user can log in successfully with the correct username and password. 
        // The returned LiveData<User> should emit a non-null User object.
        // TODO implement test
    }

    @Test
    fun login_with_incorrect_password() {
        // Test login with a valid username but an incorrect password. 
        // The returned LiveData<User> should emit a null value.
        // TODO implement test
    }

    @Test
    fun login_with_non_existent_username() {
        // Attempt to log in with a username that does not exist in the database. 
        // The returned LiveData<User> should emit a null value.
        // TODO implement test
    }

    @Test
    fun login_with_empty_credentials() {
        // Test login with an empty string for both username and password. 
        // The LiveData should emit null.
        // TODO implement test
    }

    @Test
    fun login_with_null_credentials() {
        // Test login with null values for username and password. 
        // This will likely test the underlying Dao's query handling for null parameters.
        // TODO implement test
    }

    @Test
    fun getUserId_for_existing_user() {
        // Verify that providing an existing username returns a LiveData object that emits the correct user ID (a Long value).
        // TODO implement test
    }

    @Test
    fun getUserId_for_non_existent_user() {
        // Test getting a user ID for a username that is not in the database. 
        // The returned LiveData should emit a null value.
        // TODO implement test
    }

    @Test
    fun getUserId_with_null_username() {
        // Call getUserId with a null username to check for null safety. 
        // The LiveData should emit null.
        // TODO implement test
    }

    @Test
    fun getUserFirstName_for_existing_user_ID() {
        // Verify that a valid and existing user ID returns a LiveData object that emits the correct first name string.
        // TODO implement test
    }

    @Test
    fun getUserFirstName_for_non_existent_user_ID() {
        // Test with a user ID (e.g., -1L or a large number) that does not correspond to any user. 
        // The returned LiveData should emit a null value.
        // TODO implement test
    }

    @Test
    fun userExists_for_an_existing_user() {
        // Check that for an existing username, the returned LiveData emits the boolean value 'true'.
        // TODO implement test
    }

    @Test
    fun userExists_for_a_non_existent_user() = runTest {
        val nonExistentUsername = "NonExistentUser"

        // Mock the DAO Flow
        coEvery { userDao.userExists(nonExistentUsername) } returns flowOf(false)

        // Call the repository method
        val resultFlow: Flow<Boolean> = userRepository.userExists(nonExistentUsername)

        // Collect the first value and assert
        val result = resultFlow.first()
        assertEquals(false, result)
    }

    @Test
    fun userExists_with_case_sensitive_username() {
        // Test if 'Username' exists when 'username' is what's stored in the database to verify case sensitivity. 
        // The result should be 'false' assuming the query is case-sensitive.
        // TODO implement test
    }

    @Test
    fun register_a_new_unique_user() = runTest {
        // 1. Create a new user
        val newUser = User("Mock", "User", "mockuser", "password")

        // 2. Mock DAO insertUser to return an ID (e.g., 42L)
        coEvery { userDao.insertUser(newUser) } returns 42L

        // 3. Call the repository method
        userRepository.registerUser(newUser)

        // 4. Verify that insertUser was called
        coVerify(exactly = 1) { userDao.insertUser(newUser) }

        // 5. Optionally assert that the user ID was set correctly
        assertEquals(42L, newUser.id)
    }


    @Test
    fun register_user_with_a_null_object() {
        // Attempt to register a null User object. 
        // This should be handled gracefully, likely throwing a NullPointerException or a similar error within the executor thread.
        // TODO implement test
    }

    @Test
    fun register_user_and_verify_thread_execution() {
        // Verify that the userDao.insertUser method is called on a background thread managed by executorService.
        // TODO implement test
    }

    @Test
    fun checkForExistingUsername_with_an_existing_username() {
        // Call the method with a username that is already in the database. 
        // Verify that the callback's onUsernameExists method is invoked with 'true'.
        // TODO implement test
    }

    @Test
    fun checkForExistingUsername_with_a_new_username() {
        // Call the method with a username that is not in the database. 
        // Verify that the callback's onUsernameExists method is invoked with 'false'.
        // TODO implement test
    }

    @Test
    fun checkForExistingUsername_callback_execution_on_background_thread() {
        // Verify that the database check and the callback invocation happen on the executorService's background thread.
        // TODO implement test
    }

    @Test
    fun deleteAll_with_a_populated_database() {
        // Add multiple users to the database and then call deleteAll(). 
        // Verify that the database is empty afterwards.
        // TODO implement test
    }

    @Test
    fun deleteAll_with_an_empty_database() {
        // Call deleteAll() on an already empty database. 
        // Verify that the operation completes without errors and the database remains empty.
        // TODO implement test
    }
}