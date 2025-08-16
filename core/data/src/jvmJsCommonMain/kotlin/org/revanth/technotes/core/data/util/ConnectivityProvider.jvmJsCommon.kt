/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.core.data.util

import dev.jordond.connectivity.Connectivity

actual val connectivityProvider: Connectivity
    get() = Connectivity {
        autoStart = true
        urls("cloudflare.com", "google.com")
        port = 80
        pollingIntervalMs = 5.minutes
        timeoutMs = 1.minutes
    }
