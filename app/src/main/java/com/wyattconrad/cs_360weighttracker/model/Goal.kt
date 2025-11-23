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
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * The goal entity
 * @author Wyatt Conrad
 * @version 1.0
 */
// Set the username as unique
// Set a foreign key to the user table
// Set the child column on delete method
@Entity(
    tableName = "goals",
    indices = [Index(value = arrayOf("user_id"), unique = true)],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)]
)
data class Goal(

    // Create the id as the primary key
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    // Create the date time set
    @ColumnInfo(name = "date_time_set")
    val dateTimeSet: Long,

    // Create the goal
    @field:ColumnInfo(name = "goal")
    val goal: Double,

    // Create the user id
    @field:ColumnInfo(name = "user_id")
    val userId: Long

)
