/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package cmp.navigation.authenticatednavbar

import androidx.compose.ui.graphics.vector.ImageVector
import cmp.navigation.generated.resources.Res
import cmp.navigation.generated.resources.home
import cmp.navigation.generated.resources.profile
import cmp.navigation.generated.resources.settings
import cmp.navigation.utils.toObjectNavigationRoute
import org.jetbrains.compose.resources.StringResource
import org.revanth.technotes.core.designsystem.icon.AppIcons
import org.revanth.technotes.core.ui.NavigationItem
import org.revanth.technotes.feature.home.TasksDestination
import org.revanth.technotes.feature.home.TasksRoute
import org.revanth.technotes.feature.settings.SettingsRoute

sealed class AuthenticatedNavBarTabItem : NavigationItem {

    data object HomeTab : AuthenticatedNavBarTabItem() {
        override val selectedIcon: ImageVector
            get() = AppIcons.HomeBoarder
        override val icon: ImageVector
            get() = AppIcons.Home
        override val labelRes: StringResource
            get() = Res.string.home
        override val contentDescriptionRes: StringResource
            get() = Res.string.home
        override val graphRoute: String
            get() = TasksDestination.toObjectNavigationRoute()
        override val startDestinationRoute: String
            get() = TasksDestination.toObjectNavigationRoute()
        override val testTag: String
            get() = "HomeTab"
    }

    data object SettingsTab : AuthenticatedNavBarTabItem() {
        override val selectedIcon: ImageVector
            get() = AppIcons.Settings
        override val icon: ImageVector
            get() = AppIcons.SettingsOutlined
        override val labelRes: StringResource
            get() = Res.string.settings
        override val contentDescriptionRes: StringResource
            get() = Res.string.settings
        override val graphRoute: String
            get() = SettingsRoute.toObjectNavigationRoute()
        override val startDestinationRoute: String
            get() = SettingsRoute.toObjectNavigationRoute()
        override val testTag: String
            get() = "SettingsTab"
    }
}
