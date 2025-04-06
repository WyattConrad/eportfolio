package com.wyattconrad.cs_360weighttracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE))
public class Weight {

    // Create the fields for the database
    // Primary key and ID for the weight
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    // Weight value
    @ColumnInfo(name = "weight")
    private double mWeight;

    // Date and time logged
    @ColumnInfo(name = "date_time_logged")
    private long mDateTimeLogged;

    // User ID that logged the weight, foreign key from user table
    @ColumnInfo(name = "user_id")
    private long mUserId;

    // Constructor for creating a new weight
    public Weight(double weight, long mUserId) {
        mWeight = weight;
        this.mUserId = mUserId;
        mDateTimeLogged = System.currentTimeMillis();
    }

    public Weight(double weight, long mUserId, LocalDateTime datetimelogged) {
        mWeight = weight;
        this.mUserId = mUserId;
        // Convert LocalDateTime to long
        ZoneId zoneId = ZoneId.systemDefault();
        mDateTimeLogged = datetimelogged.atZone(zoneId).toInstant().toEpochMilli();
    }

    // Getters and Setters
    // Get the id
    public long getId() {
        return mId;
    }
    // Set the id
    public void setId(long id) {
        mId = id;
    }

    // Get the weight
    public double getWeight() {
        return mWeight;
    }
    // Set the weight
    public void setWeight(double weight) {
        mWeight = weight;
    }

    // Get the date and time logged
    public long getDateTimeLogged() {
        return mDateTimeLogged;
    }
    // Set the date and time logged
    public void setDateTimeLogged(long datetimelogged) {
        mDateTimeLogged = datetimelogged;
    }

    // Get the user id
    public long getUserId() {
        return mUserId;
    }
    // Set the user id
    public void setUserId(long userid) {
        mUserId = userid;
    }

}
