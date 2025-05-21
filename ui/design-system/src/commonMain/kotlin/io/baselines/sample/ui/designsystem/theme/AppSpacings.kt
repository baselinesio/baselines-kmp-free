package io.baselines.sample.ui.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalSpacings = staticCompositionLocalOf { AppSpacings() }

internal val DefaultSpacings = AppSpacings(
    elementSmall = 4.dp,
    elementMedium = 8.dp,
    elementLarge = 16.dp,
    layoutSmall = 16.dp,
    layoutMedium = 24.dp,
    layoutLarge = 32.dp,
)

@Immutable
data class AppSpacings(
    val elementSmall: Dp = 0.dp,
    val elementMedium: Dp = 0.dp,
    val elementLarge: Dp = 0.dp,
    val layoutSmall: Dp = 0.dp,
    val layoutMedium: Dp = 0.dp,
    val layoutLarge: Dp = 0.dp,
)
