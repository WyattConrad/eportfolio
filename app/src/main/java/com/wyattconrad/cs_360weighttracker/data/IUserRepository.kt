package com.wyattconrad.cs_360weighttracker.data

import com.wyattconrad.cs_360weighttracker.data.UserRepository.UsernameCallback
import com.wyattconrad.cs_360weighttracker.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun login(username: String?, password: String?): Flow<User?>
    fun getUserId(username: String?): Flow<Long?>
    fun getUserFirstName(userId: Long): Flow<String?>
    fun userExists(username: String?): Flow<Boolean?>
    fun fetchUser(userId: Long): Flow<User?>
    fun getUsername(userId: Long): Flow<String?>
    suspend fun registerUser(user: User)
    fun checkForExistingUsername(username: String?, callback: UsernameCallback)
    fun checkForExistingUsername(username: String) : Boolean
    suspend fun deleteAll()
}