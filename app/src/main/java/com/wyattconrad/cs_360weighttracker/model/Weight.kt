package com.wyattconrad.cs_360weighttracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

// Set a foreign key to the user table
@Entity(
    tableName = "weights",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)])
data class Weight (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "weight")
    var weight: Double,

    @ColumnInfo(name = "date_time_logged")
    val dateTimeLogged: LocalDateTime,

    @ColumnInfo(name = "user_id")
    val userId: Long,
) {
    // Constructor for creating a new weight
    constructor(weight: Double, userId: Long) : this(
        weight = weight,
        userId = userId,
        dateTimeLogged = LocalDateTime.now()
    )
}
