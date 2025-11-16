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


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weight_database.db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val database = provideAppDatabase(context)
                    addStarterData(
                        database.userDao,
                        database.weightDao,
                        database.goalDao
                    )
                }
            }
        }).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: AppDatabase): UserRepository {
        return UserRepository(db.userDao)
    }

    @Provides
    @Singleton
    fun provideGoalRepository(db: AppDatabase): GoalRepository {
        return GoalRepository(db.goalDao)
    }

    @Provides
    @Singleton
    fun provideWeightRepository(db: AppDatabase): WeightRepository {
        return WeightRepository(db.weightDao)
    }

    @Provides
    @Singleton // Only one LoginService instance
    fun provideLoginService(@ApplicationContext context: Context): LoginService {
        return LoginService(context)
    }

}

//*
//Add some starter data to the database
//*
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

    val userId = userDao.insertUser(user)

    // Generate weights from 160 → 141 (10 days)
    val startDate = LocalDateTime.now().minusDays(20)

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
