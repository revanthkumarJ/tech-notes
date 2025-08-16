/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.core.data.repositoryImpl

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.revanth.technotes.core.data.model.LogoutEvent
import org.revanth.technotes.core.data.model.LogoutReason
import org.revanth.technotes.core.data.repository.UserLogoutManager
import org.revanth.technotes.core.data.util.bufferedMutableSharedFlow
import org.revanth.technotes.core.datastore.UserPreferencesRepository
import template.core.base.common.manager.DispatcherManager

class UserLogoutManagerImpl(
    private val repository: UserPreferencesRepository,
    dispatcherManager: DispatcherManager,
) : UserLogoutManager {

    private val scope = CoroutineScope(dispatcherManager.unconfined)

    private val mutableLogoutEventFlow: MutableSharedFlow<LogoutEvent> = bufferedMutableSharedFlow()
    override val logoutEventFlow: SharedFlow<LogoutEvent> = mutableLogoutEventFlow.asSharedFlow()

    /**
     * Completely logs out the given [userId], removing all data. The [reason] indicates why the
     * user is being logged out.
     */
    // TODO:: Currently, both methods (logout and softLogout) perform the same action.
    override fun logout(userId: Long, reason: LogoutReason) {
        Logger.d { "User Logout - $userId, $reason" }

        clearUserData()
        mutableLogoutEventFlow.tryEmit(LogoutEvent(userId))
    }

    /**
     * Partially logs out the given [userId]. All data for the given [userId] will be removed with
     * the exception of basic account data. The [reason] indicates why the user is being logged out.
     */
    override fun softLogout(userId: Long, reason: LogoutReason) {
        Logger.d { "User Logout - $userId, $reason" }

        clearUserData()
        mutableLogoutEventFlow.tryEmit(LogoutEvent(userId))
    }

    private fun clearUserData() {
        scope.launch {
            // repository.clearAccountData()
            repository.clearUserData()
        }
    }
}
