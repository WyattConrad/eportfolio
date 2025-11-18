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
package com.wyattconrad.cs_360weighttracker.di

import android.content.Context
import android.telephony.SmsManager
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wyattconrad.cs_360weighttracker.data.AppDatabase
import com.wyattconrad.cs_360weighttracker.data.GoalDao
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.data.UserDao
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.data.WeightDao
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.model.Goal
import com.wyattconrad.cs_360weighttracker.model.User
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.LoginService
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Singleton


/**
 * App module for dependency injection.
 * Provides singleton instances of database, repositories, and login service.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Singleton instances of database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        // Create the database
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weight_database.db"
            // Add a callback to add starter data to the database
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Launch a coroutine to add starter data to the database
                CoroutineScope(Dispatchers.IO).launch {
                    val database = provideAppDatabase(context)
                    // Add starter data to the database
                    addStarterData(
                        database.userDao,
                        database.weightDao,
                        database.goalDao
                    )
                }
            }
        }).build()
    }

    // Singleton instances of User Repository
    @Provides
    @Singleton
    fun provideUserRepository(db: AppDatabase): UserRepository {
        return UserRepository(db.userDao)
    }

    // Singleton instances of Goal Repository
    @Provides
    @Singleton
    fun provideGoalRepository(db: AppDatabase): GoalRepository {
        return GoalRepository(db.goalDao)
    }

    // Singleton instances of Weight Repository
    @Provides
    @Singleton
    fun provideWeightRepository(db: AppDatabase): WeightRepository {
        return WeightRepository(db.weightDao)
    }

    // Singleton instance of LoginService
    @Provides
    @Singleton
    fun provideLoginService(@ApplicationContext context: Context): LoginService {
        return LoginService(context)
    }

    @Provides
    @Singleton
    fun provideSmsManager(@ApplicationContext context: Context): SmsManager {
        return context.getSystemService(SmsManager::class.java)
    }

    @Provides
    @Singleton
    fun provideUserPreferenceService(@ApplicationContext context: Context): UserPreferencesService {
        return UserPreferencesService(context)
    }

}

/**
 * Add starter data to the database
 *
 * @param userDao User DAO
 * @param weightDao Weight DAO
 * @param goalDao Goal DAO
 *
 * @author Wyatt Conrad
 * @version 1.0
*/
suspend fun addStarterData(
    userDao: UserDao,
    weightDao: WeightDao,
    goalDao: GoalDao
) {
    // Create sample user
    val user = User(
        firstName = "Guest",
        lastName = "User",
        username = "guest",
        password = "password@123"
    )

    // Insert user into database and get the user ID
    val userId = userDao.insertUser(user)

    // Generate weights from 160 → 141 (10 days)
    val startDate = LocalDateTime.now().minusDays(20)

    // Create the weights and add them to the database
    (160 downTo 141).forEachIndexed { index, weightValue ->
        weightDao.insertWeight(
            Weight(
                id = 0L,
                weight = weightValue.toDouble(),
                dateTimeLogged = startDate.plusDays(index.toLong()),
                userId = userId
            )
        )
    }

    // Add a sample goal
    goalDao.insertGoal(
        Goal(
            goal = 135.0,
            userId = userId
        )
    )
}
