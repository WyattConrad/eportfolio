package com.wyattconrad.cs_360weighttracker.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.data.UserRepository.UsernameCallback
import com.wyattconrad.cs_360weighttracker.model.User
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Register new user
    suspend fun registerUser(user: User) {
        userRepository.registerUser(user)
    }

    fun registerUserCoroutine(user: User){
        viewModelScope.launch {
            registerUser(user)
        }
    }

    // Login user
    fun login(username: String?, password: String?): LiveData<User?> {
        return userRepository.login(username, password).asLiveData()
    }

    // Check if username already exists
    fun checkForExistingUsername(username: String?, callback: UsernameCallback) {
        userRepository.checkForExistingUsername(username, callback)
    }
}