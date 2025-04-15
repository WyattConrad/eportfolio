package com.wyattconrad.cs_360weighttracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// Set the username as unique
// Set a foreign key to the user table
// Set the child column on delete method
@Entity(indices = {@Index(value = "user_id", unique = true)},
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id",
        childColumns = "user_id", onDelete = ForeignKey.CASCADE))
public class Goal {

    // Create the fields for the database
    // Primary key and ID for the goal
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    // Goal value
    @ColumnInfo(name = "goal")
    private double mGoal;

    // Date and time set
    @ColumnInfo(name = "date_time_set")
    private long mDateTimeSet;

    // User ID that set the goal, foreign key from user table
    @ColumnInfo(name = "user_id")
    private long mUserId;


    /**
     * Default constructor
     * @param goal
     * @param mUserId
     */
    public Goal(double goal, long mUserId) {
        mGoal = goal;
        mDateTimeSet = System.currentTimeMillis();
        this.mUserId = mUserId;
    }

    // Getters and Setters
    public long getId() {
        return mId;
    }

    // Setter for Id
    public void setId(long id) {
        mId = id;
    }

    // Getter for Goal
    public double getGoal() {
        return mGoal;
    }

    // Setter for Goal
    public void setGoal(double goal) {
        mGoal = goal;
    }

    // Getter for DateTimeSet
    public long getDateTimeSet() {
        return mDateTimeSet;
    }

    // Setter for DateTimeSet
    public void setDateTimeSet(long dateTimeSet) {
        mDateTimeSet = dateTimeSet;
    }

    // Getter for UserId
    public long getUserId() {
        return mUserId;
    }

    // Setter for UserId
    public void setUserId(long userid) {
        mUserId = userid;
    }

}
