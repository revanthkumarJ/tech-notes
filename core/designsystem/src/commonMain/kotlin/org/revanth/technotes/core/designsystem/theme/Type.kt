/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package org.revanth.technotes.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import org.revanth.technotes.core.designsystem.generated.resources.Res
import org.revanth.technotes.core.designsystem.generated.resources.outfit_black
import org.revanth.technotes.core.designsystem.generated.resources.outfit_bold
import org.revanth.technotes.core.designsystem.generated.resources.outfit_extra_bold
import org.revanth.technotes.core.designsystem.generated.resources.outfit_extra_light
import org.revanth.technotes.core.designsystem.generated.resources.outfit_light
import org.revanth.technotes.core.designsystem.generated.resources.outfit_medium
import org.revanth.technotes.core.designsystem.generated.resources.outfit_regular
import org.revanth.technotes.core.designsystem.generated.resources.outfit_semi_bold
import org.revanth.technotes.core.designsystem.generated.resources.outfit_thin

val fontFamily: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.outfit_black, FontWeight.Black),
        Font(Res.font.outfit_bold, FontWeight.Bold),
        Font(Res.font.outfit_semi_bold, FontWeight.SemiBold),
        Font(Res.font.outfit_medium, FontWeight.Medium),
        Font(Res.font.outfit_regular, FontWeight.Normal),
        Font(Res.font.outfit_light, FontWeight.Light),
        Font(Res.font.outfit_thin, FontWeight.Thin),
        Font(Res.font.outfit_extra_light, FontWeight.ExtraLight),
        Font(Res.font.outfit_extra_bold, FontWeight.ExtraBold),
    )
