package com.wyattconrad.cs_360weighttracker

import android.app.Application
import com.wyattconrad.cs_360weighttracker.repo.WeightRepository
import com.wyattconrad.cs_360weighttracker.repo.UserRepository
import com.wyattconrad.cs_360weighttracker.repo.GoalRepository
import com.wyattconrad.cs_360weighttracker.repo.AppDatabase

class WeightTrackerApplication : Application() {

    // This is where you initialize singletons
    val database by lazy { AppDatabase.getDatabase(this) }
    val weightRepository by lazy { WeightRepository(this) }
    val userRepository by lazy { UserRepository(this) }
    val goalRepository by lazy { GoalRepository(this) }
}
