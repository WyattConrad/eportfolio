package com.wyattconrad.cs_360weighttracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wyattconrad.cs_360weighttracker.model.Goal
import com.wyattconrad.cs_360weighttracker.model.User
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.Converters

@Database(entities = [User::class, Weight::class, Goal::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val weightDao: WeightDao
    abstract val goalDao: GoalDao

    /*companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private const val number_of_threads = 4
        @JvmField
        val databaseWriteExecutor: ExecutorService? = Executors.newFixedThreadPool(number_of_threads)

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    if (instance == null) {
                        instance = databaseBuilder<AppDatabase?>(context.getApplicationContext(), AppDatabase::class.java, "weight_database.db").addCallback(
                            object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)

                                    // Use the ExecutorService to perform database operations on a background thread
                                    Executors.newSingleThreadExecutor().execute(Runnable {
                                        val database: AppDatabase = getDatabase(context)
                                        val userDao = database.userDao()
                                        val weightDao = database.weightDao()
                                        val goalDao = database.goalDao()

                                        // Add starter data to the database
                                        addStarterData(userDao, weightDao, goalDao)
                                    })
                                }
                            }).build()
                    }
                }
            }
            return instance!!
        }

        *//***
         * Add some starter data to the database
         *//*
        private fun addStarterData(userDao: UserDao, weightDao: WeightDao, goalDao: GoalDao) {
            // Create a user
            val user = User("Guest", "User", "guest", "password@123")
            user.id = -1
            val userId = userDao.insertUser(user)

            Log.d("WeightRepository", "User added with ID: " + userId)
            Log.d("WeightRepository", "User added with username: " + user.username)

            // Set a starter date time for the initial weight
            var dateTimeLogged = LocalDateTime.now().minusDays(10)

            // Add some sample weights
            val weight = Weight(0L, 150.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight)
            dateTimeLogged = LocalDateTime.now().minusDays(9)
            val weight2 = Weight(0L, 149.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight2)
            dateTimeLogged = LocalDateTime.now().minusDays(8)
            val weight3 = Weight(0L, 148.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight3)
            dateTimeLogged = LocalDateTime.now().minusDays(7)
            val weight4 = Weight(0L, 147.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight4)
            dateTimeLogged = LocalDateTime.now().minusDays(6)
            val weight5 = Weight(0L, 146.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight5)
            dateTimeLogged = LocalDateTime.now().minusDays(5)
            val weight6 = Weight(0L, 145.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight6)
            dateTimeLogged = LocalDateTime.now().minusDays(4)
            val weight7 = Weight(0L, 144.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight7)
            dateTimeLogged = LocalDateTime.now().minusDays(3)
            val weight8 = Weight(0L, 143.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight8)
            dateTimeLogged = LocalDateTime.now().minusDays(2)
            val weight9 = Weight(0L, 142.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight9)
            dateTimeLogged = LocalDateTime.now().minusDays(1)
            val weight10 = Weight(0L, 141.0, dateTimeLogged, userId)
            weightDao.insertWeight(weight10)

            // Add a sample goal
            val goal = Goal(135.0, userId)
            goalDao.insertGoal(goal)
        }
    }*/
}
