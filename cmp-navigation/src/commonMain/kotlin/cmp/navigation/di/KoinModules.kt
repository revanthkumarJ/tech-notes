/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package cmp.navigation.di

import cmp.navigation.AppViewModel
import cmp.navigation.authenticatednavbar.AuthenticatedNavbarNavigationViewModel
import cmp.navigation.rootnav.RootNavViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.revanth.technotes.core.data.di.DataModule
import org.revanth.technotes.core.database.di.DatabaseModule
import org.revanth.technotes.core.datastore.di.DatastoreModule
import org.revanth.technotes.feature.home.di.HomeModule
import org.revanth.technotes.feature.settings.SettingsModule
import template.core.base.analytics.di.analyticsModule
import template.core.base.common.di.CommonModule
import template.core.base.platform.di.platformModule

object KoinModules {
    private val dataModule = module {
        includes(DataModule)
    }

    private val dispatcherModule = module {
        includes(CommonModule)
    }

    private val AppModule = module {
        includes(platformModule)

        viewModelOf(::AppViewModel)
        viewModelOf(::AuthenticatedNavbarNavigationViewModel)
        viewModelOf(::RootNavViewModel)
    }

    private val featureModule = module {
        includes(
            HomeModule,
            SettingsModule,
        )
    }

    val allModules = listOf(
        dataModule,
        DatabaseModule,
        dispatcherModule,
        analyticsModule,
        DatastoreModule,
        featureModule,
        AppModule,
    )
}
