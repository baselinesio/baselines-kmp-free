package io.baselines.sample.ui.designsystem.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

internal val LocalShapes = staticCompositionLocalOf { AppShapes() }

internal val DefaultShapes = AppShapes(
    roundSmall = RoundedCornerShape(8.dp),
    roundMedium = RoundedCornerShape(16.dp),
    roundLarge = RoundedCornerShape(24.dp),
)

@Immutable
data class AppShapes(
    val roundSmall: CornerBasedShape = ShapeDefaults.Small,
    val roundMedium: CornerBasedShape = ShapeDefaults.Medium,
    val roundLarge: CornerBasedShape = ShapeDefaults.Large,
    val roundBottomLarge: CornerBasedShape = RoundedCornerShape(
        topStart = CornerSize(0.dp),
        topEnd = CornerSize(0.dp),
        bottomStart = roundLarge.bottomStart,
        bottomEnd = roundLarge.bottomEnd,
    ),
    val roundTopMedium: CornerBasedShape = RoundedCornerShape(
        topStart = roundMedium.topStart,
        topEnd = roundMedium.topEnd,
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp),
    ),
)
