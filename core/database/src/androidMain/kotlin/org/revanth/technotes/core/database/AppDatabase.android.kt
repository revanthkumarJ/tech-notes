/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.revanth.technotes.core.database.dao.SampleDao
import org.revanth.technotes.core.database.entity.SampleEntity
import org.revanth.technotes.core.database.utils.ChargeTypeConverters

@Database(
    entities = [
        SampleEntity::class,
    ],
    version = AppDatabase.VERSION,
    exportSchema = true,
    autoMigrations = [],
)
@TypeConverters(ChargeTypeConverters::class)
actual abstract class AppDatabase : RoomDatabase() {

    actual abstract val sampleDao: SampleDao

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "mifos_database.db"
    }
}
