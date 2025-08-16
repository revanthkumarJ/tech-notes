/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.TimeZone

/**
 * Utility for reporting current timezone the device has set.
 * It always emits at least once with default setting and then for each TZ change.
 */

interface TimeZoneMonitor {
    val currentTimeZone: Flow<TimeZone>
}
