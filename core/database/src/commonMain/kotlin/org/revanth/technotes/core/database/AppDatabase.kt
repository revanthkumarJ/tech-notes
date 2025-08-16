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

import org.revanth.technotes.core.database.dao.SampleDao

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect abstract class AppDatabase {
    abstract val sampleDao: SampleDao
}
