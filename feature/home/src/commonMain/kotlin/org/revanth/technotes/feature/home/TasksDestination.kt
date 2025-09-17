/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import org.revanth.technotes.feature.home.addNote.AddNoteScreen
import org.revanth.technotes.feature.home.home.HomeScreen
import template.core.base.ui.composableWithPushTransitions
import template.core.base.ui.composableWithStayTransitions

@Serializable
data object TasksDestination

@Serializable
data object TasksRoute

@Serializable
data class AddEditTaskRoute(
    val taskId: Int? = null,
)

fun NavController.navigateToTasks(navOptions: NavOptions? = null) {
    navigate(TasksDestination, navOptions)
}

fun NavController.navigateToAddEditTask(taskId: Int? = null, navOptions: NavOptions? = null) {
    navigate(AddEditTaskRoute(taskId), navOptions)
}

fun NavGraphBuilder.tasksGraph(
    navController: NavController,
) {
    navigation<TasksDestination>(
        startDestination = TasksRoute,
    ) {
        composableWithStayTransitions<TasksRoute> {
            HomeScreen(
                navigateToAddNote = navController::navigateToAddEditTask,
                navigateToEditNote = {},
            )
        }

        composableWithPushTransitions<AddEditTaskRoute> {
            AddNoteScreen(
                navigateBack = navController::popBackStack
            )
        }
    }
}
