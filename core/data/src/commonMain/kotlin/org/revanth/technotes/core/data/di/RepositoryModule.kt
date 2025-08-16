/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.core.data.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.revanth.technotes.core.data.repository.NetworkMonitor
import org.revanth.technotes.core.data.repository.UserDataRepository
import org.revanth.technotes.core.data.repository.UserLogoutManager
import org.revanth.technotes.core.data.repositoryImpl.NetworkMonitorImpl
import org.revanth.technotes.core.data.repositoryImpl.UserDataRepositoryImpl
import org.revanth.technotes.core.data.repositoryImpl.UserLogoutManagerImpl
import org.revanth.technotes.core.datastore.di.DatastoreModule
import template.core.base.common.di.CommonModule

val DataModule = module {
    includes(platformModule, CommonModule, DatastoreModule)

    singleOf(::NetworkMonitorImpl) bind NetworkMonitor::class
    singleOf(::UserDataRepositoryImpl) bind UserDataRepository::class
    singleOf(::UserLogoutManagerImpl) bind UserLogoutManager::class
}

expect val platformModule: Module
