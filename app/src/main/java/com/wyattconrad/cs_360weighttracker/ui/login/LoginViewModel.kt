package com.wyattconrad.cs_360weighttracker.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.model.User
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getText() : LiveData<String> {
        return MutableLiveData<String>("Please Login")
    }

    fun login(username: String?, password: String?): LiveData<User?> {
        return userRepository.login(username, password).asLiveData()
    }

    fun getUserId(username: String?): LiveData<Long?> {
        return userRepository.getUserId(username).asLiveData()
    }
}