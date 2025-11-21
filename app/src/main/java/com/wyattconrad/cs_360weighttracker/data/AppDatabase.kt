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
package com.wyattconrad.cs_360weighttracker.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import at.favre.lib.crypto.bcrypt.BCrypt
import com.wyattconrad.cs_360weighttracker.model.Goal
import com.wyattconrad.cs_360weighttracker.model.User
import com.wyattconrad.cs_360weighttracker.model.Weight
import com.wyattconrad.cs_360weighttracker.service.Converters


/**
 * The Room database for this app
 * @author Wyatt Conrad
 * @version 3.0
 */
@Database(
    entities = [User::class, Weight::class, Goal::class],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 3, to = 4, spec = Migration3To4::class)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // Add the DAOs which will represent the tables in the database
    abstract val userDao: UserDao
    abstract val weightDao: WeightDao
    abstract val goalDao: GoalDao

    // Migration of the Db from version 3 to version 4
    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {

                // 1. Hash all existing passwords into the new column
                val cursor = db.query("SELECT id, password FROM users")
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(0)
                    val plain = cursor.getString(1)

                    val hashed = hash(plain)

                    db.execSQL(
                        "UPDATE users SET hashed_password = ? WHERE id = ?",
                        arrayOf(hashed, id)
                    )
                }
                cursor.close()

            }
        }
    }
}

@DeleteColumn(tableName = "users", columnName = "password")
class Migration3To4 : AutoMigrationSpec {}

// Hash a password using bcrypt
fun hash(password: String): String {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray())
}