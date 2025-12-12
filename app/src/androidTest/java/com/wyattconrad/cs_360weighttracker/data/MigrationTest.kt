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

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Tests for database migrations.
 *
 * @author Wyatt Conrad
 * @version 1.0
 */
@RunWith(AndroidJUnit4::class)
class MigrationTest {
    // Database name for testing
    private val TEST_DB = "migration-test.db"

    private val hashingService = com.wyattconrad.cs_360weighttracker.service.HashingService()

    // Array of all migrations
    private val ALL_MIGRATIONS = arrayOf(AppDatabase.MIGRATION_2_3)

    // Helper for testing database migrations
    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    // Migration from version 1 to version 2
    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        // Create earliest version of the database.
        var db = helper.createDatabase(TEST_DB, 1).apply {
            execSQL("INSERT INTO users (id, first_name, last_name, username, password) VALUES (1, 'Guest', 'User', 'guest', 'password')")

        // Prepare for the next version.
            close()
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true)

        // Validate that the data was migrated properly.
        db.query("SELECT * FROM users").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getInt(getColumnIndex("id"))).isEqualTo(1)
        }

    }

    // Test all migrations
    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        // Create version 2 of the database.
        var db = helper.createDatabase(TEST_DB, 2).apply {
            execSQL("INSERT INTO users (id, first_name, last_name, username, password) VALUES (1, 'Guest', 'User', 'guest', 'password')")

            // Prepare for the next version.
            //close()
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_2_3 as the migration process.
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
            TEST_DB
        ).addMigrations(*ALL_MIGRATIONS).build().apply {
            openHelper.writableDatabase.close()
        }

        val hashedPassword = hashingService.hashPassword("password")

        // Validate that the data was migrated properly.
        db.query("SELECT * FROM users").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getInt(getColumnIndex("id"))).isEqualTo(1)
            assertThat(hashingService.verifyPassword("password", getString(getColumnIndex("password")))).isEqualTo(true)
        }
    }
}