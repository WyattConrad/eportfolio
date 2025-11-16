package com.wyattconrad.cs_360weighttracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Set the username as unique
// Set a foreign key to the user table
// Set the child column on delete method
@Entity(
    tableName = "goals",
    indices = [Index(value = arrayOf("user_id"), unique = true)],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)]
)
class Goal(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "date_time_set")
    var dateTimeSet: Long,

    @field:ColumnInfo(name = "goal")
    var goal: Double,

    @field:ColumnInfo(name = "user_id")
    var userId: Long

) {
    /**
     * Default constructor
     * @param goal The goal value
     * @param userId The user Id
     */
    constructor(goal: Double, userId: Long) : this(
        id = 0L,
        goal = goal,
        dateTimeSet = System.currentTimeMillis(),
        userId = userId
    )
}
