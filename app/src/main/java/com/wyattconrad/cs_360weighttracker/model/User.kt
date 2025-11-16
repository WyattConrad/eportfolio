package com.wyattconrad.cs_360weighttracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Set the username as unique
@Entity(
    tableName = "users",
    indices = [Index(value = arrayOf("username"), unique = true)])
data class User (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "first_name")
    var firstName: String,

    @ColumnInfo(name = "last_name")
    var lastName: String?,

    @ColumnInfo(name = "username")
    var username: String,

    @ColumnInfo(name = "password")
    var password: String

) {
    // Constructor for creating a new user
    constructor(firstName: String, lastName: String?, username: String, password: String) : this (
        id = 0L,
        firstName = firstName,
        lastName = lastName,
        username = username,
        password = password
    )
}
