package com.wyattconrad.cs_360weighttracker.data


import com.wyattconrad.cs_360weighttracker.model.User
import kotlinx.coroutines.flow.Flow


class UserRepository(
    private val userDao: UserDao,

) : IUserRepository {

    //Login method
    override fun login(username: String?, password: String?): Flow<User?> {
        return userDao.login(username, password)
    }

    // Get User Id
    override fun getUserId(username: String?): Flow<Long?> {
        return userDao.getUserId(username)
    }

    // Get User First Name
    override fun getUserFirstName(userId: Long): Flow<String?> {
        return userDao.getUserFirstName(userId)
    }

    // Check if user exists
    override fun userExists(username: String?): Flow<Boolean?> {
        return userDao.userExists(username)
    }

    // Fetch the user
    override fun fetchUser(userId: Long): Flow<User?> {
        return userDao.fetchUser(userId)
    }

    override fun getUsername(userId: Long): Flow<String?> {
        return userDao.getUsername(userId)
    }

    // Register New User
    override suspend fun registerUser(user: User) {
            val userId = userDao.insertUser(user)
            user.id = userId
    }


    // Create an interface for the callback
    interface UsernameCallback {
        fun onUsernameExists(exists: Boolean)
    }

    // Check if username already exists
    override fun checkForExistingUsername(username: String?, callback: UsernameCallback) {
            // Check if the username already exists in the database
            val count = userDao.countUsersByUsername(username)
            callback.onUsernameExists(count > 0)
    }

    // Check if username already exists
    override fun checkForExistingUsername(username: String): Boolean {
            return if(userDao.countUsersByUsername(username) > 0) true else false
    }

    override suspend fun deleteAll() {
        userDao.deleteAll()
    }
}
