package com.wyattconrad.cs_360weighttracker.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.wyattconrad.cs_360weighttracker.data.AppDatabase
import com.wyattconrad.cs_360weighttracker.data.GoalRepository
import com.wyattconrad.cs_360weighttracker.data.UserRepository
import com.wyattconrad.cs_360weighttracker.data.WeightRepository
import com.wyattconrad.cs_360weighttracker.service.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "weight_database"
        ).build()
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