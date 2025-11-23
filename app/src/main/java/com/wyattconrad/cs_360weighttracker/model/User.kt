/*
 * Copyright (C) 2025 Wyatt Conrad. All rights reserved.
 *
 * This file is part of the CS-360 Weight Tracker project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wyattconrad.cs_360weighttracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * The user entity
 * @author Wyatt Conrad
 * @version 3.0
 */
// Set the username as unique
@Entity(
    tableName = "users",
    indices = [
        Index(value = arrayOf("username"), unique = true),
        Index(value = arrayOf("email"), unique = true)
    ]
)
data class User (

    // Create the id as the primary key
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = -1L,

    // Create the first name, last name, username, and password
    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String?,

    @ColumnInfo(name = "email", defaultValue = "")
    val email: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "hashed_password", defaultValue = "")
    val hashedPassword: String

)
