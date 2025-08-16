/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.core.model

data class Country(
    val code: String,
    val name: String,
    val phoneCode: String,
    val mobilePattern: String,
    val landlinePattern: String? = null,
    val specialPattern: String? = null,
    val formatExample: String,
    val flagEmoji: String,
    val flagResourceName: String,
)
