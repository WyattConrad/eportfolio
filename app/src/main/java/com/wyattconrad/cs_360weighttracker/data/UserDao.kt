package com.wyattconrad.cs_360weighttracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wyattconrad.cs_360weighttracker.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByUsername(username: String?): Flow<User?>

    @get:Query("SELECT * FROM users")
    val allUsers: Flow<MutableList<User?>?>

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    fun countUsersByUsername(username: String?): Int

    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE username = :username)")
    fun userExists(username: String?): Flow<Boolean?>

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    fun login(username: String?, password: String?): Flow<User?>

    @Query("SELECT id FROM users WHERE username = :username")
    fun getUserId(username: String?): Flow<Long?>

    @Query("SELECT first_name FROM users WHERE id = :userId")
    fun getUserFirstName(userId: Long): Flow<String?>

    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAll()


    @Query("SELECT * FROM users WHERE id = :userId")
    fun fetchUser(userId: Long): Flow<User?>

    @Query("SELECT username FROM users WHERE id = :userId")
    fun getUsername(userId: Long): Flow<String?>
}
