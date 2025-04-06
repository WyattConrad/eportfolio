package com.wyattconrad.cs_360weighttracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.time.LocalDateTime;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id",
        childColumns = "user_id", onDelete = ForeignKey.CASCADE))
public class Goal {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "goal")
    private double mGoal;

    @ColumnInfo(name = "date_time_set")
    private long mDateTimeSet;

    @ColumnInfo(name = "user_id")
    private long mUserId;

    public Goal(double goal, long mUserId) {
        mGoal = goal;
        mDateTimeSet = System.currentTimeMillis();
        this.mUserId = mUserId;
    }

    // Getters and Setters
    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public double getGoal() {
        return mGoal;
    }
    public void setGoal(double goal) {
        mGoal = goal;
    }

    public long getDateTimeSet() {
        return mDateTimeSet;
    }
    public void setDateTimeSet(long dateTimeSet) {
        mDateTimeSet = dateTimeSet;
    }

    public long getUserId() {
        return mUserId;
    }
    public void setUserId(long userid) {
        mUserId = userid;
    }

}
