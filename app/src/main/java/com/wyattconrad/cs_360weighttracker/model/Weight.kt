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
import java.time.LocalDateTime

/**
 * The weight entity
 * @author Wyatt Conrad
 * @version 1.0
 */
// Set a foreign key to the user table
@Entity(
    tableName = "weights",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["user_id"])]
)
data class Weight (

    // Create the id as the primary key
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    // Create the weight column
    @ColumnInfo(name = "weight")
    var weight: Double,

    // Create the date time logged column
    @ColumnInfo(name = "date_time_logged")
    val dateTimeLogged: LocalDateTime,

    // Create the user id column
    @ColumnInfo(name = "user_id")
    val userId: Long,
) {
    /**
     * Constructor for the weight entity
     * @param weight The weight
     * @param userId The user id
     */
    constructor(weight: Double, userId: Long) : this(
        weight = weight,
        userId = userId,
        dateTimeLogged = LocalDateTime.now()
    )
}
