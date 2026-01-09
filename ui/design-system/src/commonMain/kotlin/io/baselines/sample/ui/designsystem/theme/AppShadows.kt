package io.baselines.sample.ui.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

internal val LocalShadows = staticCompositionLocalOf { AppShadows() }

internal val AppLightSchemeShadows = AppShadows(
    level1 = Shadow(
        spread = 2.dp,
        radius = 2.dp,
        alpha = 0.3F,
        offset = DpOffset(1.dp, 1.dp),
        color = Palette.black20,
    ),
    level2 = Shadow(
        spread = 2.dp,
        radius = 6.dp,
        alpha = 0.3F,
        offset = DpOffset(1.dp, 1.dp),
        color = Palette.black20,
    ),
    level3 = Shadow(
        spread = 2.dp,
        radius = 10.dp,
        alpha = 0.3F,
        offset = DpOffset(1.dp, 1.dp),
        color = Palette.black20,
    ),
    level4 = Shadow(
        spread = 2.dp,
        radius = 14.dp,
        alpha = 0.3F,
        offset = DpOffset(1.dp, 1.dp),
        color = Palette.black20,
    ),
)

@Immutable
data class AppShadows(
    val level1: Shadow = Shadow(radius = 0.dp),
    val level2: Shadow = Shadow(radius = 0.dp),
    val level3: Shadow = Shadow(radius = 0.dp),
    val level4: Shadow = Shadow(radius = 0.dp),
    val none: Shadow = Shadow(radius = 0.dp, alpha = 0F, color = Color.Transparent),
)
