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
    roundedSmall = RoundedCornerShape(8.dp),
    roundedMedium = RoundedCornerShape(12.dp),
    roundedLarge = RoundedCornerShape(24.dp),
)

@Immutable
data class AppShapes(
    val roundedSmall: CornerBasedShape = ShapeDefaults.Small,
    val roundedMedium: CornerBasedShape = ShapeDefaults.Medium,
    val roundedLarge: CornerBasedShape = ShapeDefaults.Large,
    val roundedBottomLarge: CornerBasedShape = RoundedCornerShape(
        topStart = CornerSize(0.dp),
        topEnd = CornerSize(0.dp),
        bottomStart = roundedLarge.bottomStart,
        bottomEnd = roundedLarge.bottomEnd,
    ),
    val roundedTopMedium: CornerBasedShape = RoundedCornerShape(
        topStart = roundedMedium.topStart,
        topEnd = roundedMedium.topEnd,
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp),
    ),
)
