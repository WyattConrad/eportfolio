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
import com.wyattconrad.cs_360weighttracker.service.HashingService
import com.wyattconrad.cs_360weighttracker.service.SMSService
import com.wyattconrad.cs_360weighttracker.service.UserPreferencesService
import com.wyattconrad.cs_360weighttracker.service.roundTo2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weight_database.db"
        )
            .addCallback(object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    // Add seed data to the database
                    CoroutineScope(Dispatchers.IO).launch {
                        val database =
                            Room.databaseBuilder(context, AppDatabase::class.java, "weight_database.db")
                                .build()
                        addStarterData(
                            database.userDao,
                            database.weightDao,
                            database.goalDao
                        )
                    }
                }
            })
            .addMigrations(AppDatabase.MIGRATION_2_3)
            .build()
    }


    // Singleton instances of User Repository
    @Provides
    @Singleton
    fun provideUserRepository(db: AppDatabase): UserRepository {
        return UserRepository(db.userDao, HashingService())
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

    @Provides
    @Singleton
    fun provideHashingService(): HashingService {
        return HashingService()
    }

    @Provides
    @Singleton
    fun provideSmsService(@ApplicationContext context: Context): SMSService {
        return SMSService(context)
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
    goalDao: GoalDao,
    hashPassword: (String) -> String = { password -> HashingService().hashPassword(password) }
) {
    // Establish Constants
    val WEIGHTSTOCREATE = 200

    // Create sample user
    val user = User(
        firstName = "Guest",
        lastName = "User",
        email = "guest.user@email.com",
        username = "guest",
        hashedPassword = hashPassword("password@123")
    )

    // Insert user into database and get the user ID
    val userId = userDao.insertUser(user)

    // Generate weights from 160 → 141 (10 days)
    val startDate = LocalDateTime.now().minusDays(WEIGHTSTOCREATE.toLong())

    // Create the weights and add them to the database
    val random = kotlin.random.Random
    var weight = 200.0

    for (i in 0 until WEIGHTSTOCREATE) {
        // base trend: slow decline
        weight -= 0.25

        // weekly wave (period ~ 7 days)
        val wave = kotlin.math.sin(i / 7.0) * 6.8   // ±6.8 lbs

        // small random daily noise
        val noise = (random.nextDouble() * 0.4) - 1.2   // ±1.2 lbs

        // Calculate the daily weight
        val dailyWeight = weight + wave + noise
        weightDao.insertWeight(
            Weight(
                id = 0L,
                weight = dailyWeight.roundTo2(),
                dateTimeLogged = startDate.plusDays(i.toLong()),
                userId = userId
            )
        )
    }


    // Add a sample goal
    goalDao.insertGoal(
        Goal(
            id = 0L,
            dateTimeSet = System.currentTimeMillis(),
            goal = 135.0,
            userId = userId
        )
    )
}
