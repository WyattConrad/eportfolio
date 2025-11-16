package com.wyattconrad.cs_360weighttracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wyattconrad.cs_360weighttracker.model.User;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username")
    LiveData<User> getUserByUsername(String username);

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT COUNT(*) FROM user WHERE username = :username")
    int countUsersByUsername(String username);

    @Query("SELECT EXISTS (SELECT 1 FROM user WHERE username = :username)")
    LiveData<Boolean> userExists(String username);

    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    LiveData<User> login(String username, String password);

    @Query("SELECT id FROM user WHERE username = :username")
    LiveData<Long> getUserId(String username);

    @Query("SELECT first_name FROM user WHERE id = :userId")
    LiveData<String> getUserFirstName(long userId);

    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM user")
    void deleteAll();


    @Query("SELECT * FROM user WHERE id = :userId")
    LiveData<User> fetchUser(long userId);

    @Query("SELECT username FROM user WHERE id = :userId")
    LiveData<String> getUsername(long userId);

}
