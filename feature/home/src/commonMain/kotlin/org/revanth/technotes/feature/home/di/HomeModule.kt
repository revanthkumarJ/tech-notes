/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.feature.home.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.revanth.technotes.feature.home.service.StorageService
import org.revanth.technotes.feature.home.service.StorageServiceImpl
import org.revanth.technotes.feature.home.task.EditTaskViewModel
import org.revanth.technotes.feature.home.tasks.TasksViewModel

val HomeModule = module {
    single<StorageService> { StorageServiceImpl() }
    viewModelOf(::TasksViewModel)
    viewModelOf(::EditTaskViewModel)
}
