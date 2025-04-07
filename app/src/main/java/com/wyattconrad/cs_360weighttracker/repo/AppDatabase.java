package com.wyattconrad.cs_360weighttracker.repo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wyattconrad.cs_360weighttracker.model.Goal;
import com.wyattconrad.cs_360weighttracker.model.User;
import com.wyattconrad.cs_360weighttracker.model.Weight;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Weight.class, Goal.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WeightDao weightDao();
    public abstract GoalDao goalDao();

    private static volatile AppDatabase instance;
    private static final int number_of_threads = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(number_of_threads);

    static AppDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "weight_database.db").addCallback(
                        new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);

                                // Use the ExecutorService to perform database operations on a background thread
                                Executors.newSingleThreadExecutor().execute(() -> {
                                    AppDatabase database = AppDatabase.getDatabase(context);
                                    UserDao userDao = database.userDao();
                                    WeightDao weightDao = database.weightDao();
                                    GoalDao goalDao = database.goalDao();

                                    // Add starter data to the database
                                    addStarterData(userDao, weightDao, goalDao);
                                });
                            }
                    }).build();
                }
            }
        }
        return instance;
    }

    /***
     * Add some starter data to the database
     */
    private static void addStarterData(UserDao userDao, WeightDao weightDao, GoalDao goalDao) {
    // Create a user
    User user = new User("Admin", "User", "admin@email.com", "password@123");
    long userId = userDao.insertUser(user);

    Log.d("WeightRepository", "User added with ID: " + userId);
    Log.d("WeightRepository", "User added with username: " + user.getUsername());

    // Set a starter date time for the initial weight
    LocalDateTime dateTimeLogged = LocalDateTime.now().minusDays(10);

    // Add some sample weights
    Weight weight = new Weight(150.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight);
    dateTimeLogged = LocalDateTime.now().minusDays(9);
    Weight weight2 = new Weight(149.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight2);
    dateTimeLogged = LocalDateTime.now().minusDays(8);
    Weight weight3 = new Weight(148.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight3);
    dateTimeLogged = LocalDateTime.now().minusDays(7);
    Weight weight4 = new Weight(147.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight4);
    dateTimeLogged = LocalDateTime.now().minusDays(6);
    Weight weight5 = new Weight(146.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight5);
    dateTimeLogged = LocalDateTime.now().minusDays(5);
    Weight weight6 = new Weight(145.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight6);
    dateTimeLogged = LocalDateTime.now().minusDays(4);
    Weight weight7 = new Weight(144.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight7);
    dateTimeLogged = LocalDateTime.now().minusDays(3);
    Weight weight8 = new Weight(143.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight8);
    dateTimeLogged = LocalDateTime.now().minusDays(2);
    Weight weight9 = new Weight(142.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight9);
    dateTimeLogged = LocalDateTime.now().minusDays(1);
    Weight weight10 = new Weight(141.0, userId, dateTimeLogged);
    weightDao.insertWeight(weight10);

    // Add a sample goal
    Goal goal = new Goal(135.0, userId);
    goalDao.insertGoal(goal);
    }
}
