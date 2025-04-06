package com.wyattconrad.cs_360weighttracker.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "username", unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @NonNull
    @ColumnInfo(name = "first_name")
    private String mFirstName;

    @ColumnInfo(name = "last_name")
    private String mLastName;

    @NonNull
    @ColumnInfo(name = "username")
    private String mUsername;

    @NonNull
    @ColumnInfo(name = "password")
    private String mPassword;

    public User(@NonNull String firstName, String lastName, @NonNull String username, @NonNull String password) {
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mUsername = username;
        this.mPassword = password;
    }

    // Get username
    @NonNull
    public String getUsername() {
        return mUsername;
    }

    // Get id
    public long getId() {
        return mId;
    }

    // Get first name
    @NonNull
    public String getFirstName() {
        return mFirstName;
    }

    // Get last name
    public String getLastName() {
        return mLastName;
    }

    // Get password
    @NonNull
    public String getPassword() {
        return mPassword;
    }


    // Set the id
    public void setId(long id) {
        mId = id;
    }

    // Set the first name
    public void setFirstName(@NonNull String firstName) {
        mFirstName = firstName;
    }

    // Set the last name
    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    // Set the username
    public void setUsername(@NonNull String username) {
        mUsername = username;
    }

    // Set the password
    public void setPassword(@NonNull String password) {
        mPassword = password;
    }

}
