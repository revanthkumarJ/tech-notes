/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.revanth.technotes.core.designsystem.icon.AppIcons
import org.revanth.technotes.core.ui.scaffold.KptScaffold
import technotes.feature.settings.generated.resources.Res
import technotes.feature.settings.generated.resources.feature_settings_change_theme_placeholder_text
import technotes.feature.settings.generated.resources.feature_settings_change_theme_text
import template.core.base.analytics.AnalyticsHelper
import template.core.base.analytics.TrackScreenView
import template.core.base.analytics.rememberAnalyticsHelper
import template.core.base.ui.ShareUtils

@Composable
internal fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val analyticsHelper = rememberAnalyticsHelper()
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = {
                analyticsHelper.logSettingsDialogVisible(false)
                showSettingsDialog = false
            },
        )
    }

    SettingsScreenContent(
        modifier = modifier.fillMaxSize(),
        onBackClick = onBackClick,
        onThemeCardClick = {
            analyticsHelper.logSettingsDialogVisible(true)
            showSettingsDialog = true
        },
    )

    TrackScreenView(screenName = "SettingsScreen")
}

@Composable
internal fun SettingsScreenContent(
    onBackClick: () -> Unit,
    onThemeCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KptScaffold(
        title = "Settings",
        onNavigationIconClick = onBackClick,
        modifier = modifier,
    ) {
        Column(
            modifier=Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsItemCard(
                    onClick = onThemeCardClick,
                    icon = AppIcons.Sun,
                    text = stringResource(Res.string.feature_settings_change_theme_text)
                )

                SettingsItemCard(
                    onClick = {
                        ShareUtils.openUrl("https://revanthkumarjportfolio.vercel.app/")
                    },
                    icon = AppIcons.Profile,
                    text = "About Us"
                )

                SettingsItemCard(
                    onClick = {
                        ShareUtils.mailHelpline()
                    },
                    icon = AppIcons.Contact,
                    text = "Contact Us"
                )

                SettingsItemCard(
                    onClick = {
                        ShareUtils.openAppInfo()
                    },
                    icon = AppIcons.Info,
                    text = "App Info"
                )
            }
            SettingsScreenFooter()
        }
    }
}

@Composable
internal fun SettingsItemCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text:String
){
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(shape = RoundedCornerShape(50.dp)),
            )
            Text(
                text = text,
                modifier = Modifier.weight(1F),
            )
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    imageVector = AppIcons.ArrowRight,
                    contentDescription = text,
                )
            }
        }
    }
}

@Composable
fun SettingsScreenFooter(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextDivider(text = "Tech Notes")
        Text(text = "Developed with Love By")
        Text(
            text = "Revanth Kumar J",
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun TextDivider(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    fontWeight: FontWeight = FontWeight.SemiBold,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f, true),
        )

        Text(
            text = text,
            style = textStyle,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )

        HorizontalDivider(
            modifier = Modifier
                .weight(1f, true),
        )
    }
}
private fun AnalyticsHelper.logSettingsDialogVisible(visible: Boolean) {
    logEvent(
        type = "settings_dialog_visible",
        params = mapOf("visible" to visible.toString()),
    )
}
