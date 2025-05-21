package io.baselines.sample.ui.designsystem.ext

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Adds two [androidx.compose.foundation.layout.PaddingValues] instances together and returns the combined result.
 *
 * This operator allows intuitive composition of padding from multiple layout sources, such as
 * parent padding + internal content padding, or system insets + manual spacing.
 *
 * All values are resolved using [androidx.compose.ui.unit.LayoutDirection.Ltr] to ensure consistent
 * and predictable merging.
 *
 * ### Example
 * ```kotlin
 * val combined = contentPadding + additionalSpacing
 * ```
 *
 * @receiver The original padding values.
 * @param other The padding values to add to the receiver.
 * @return A new [androidx.compose.foundation.layout.PaddingValues] instance with summed values for all sides.
 */
@Stable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(LayoutDirection.Ltr) +
        other.calculateStartPadding(LayoutDirection.Ltr),
    top = this.calculateTopPadding() + other.calculateTopPadding(),
    end = this.calculateEndPadding(LayoutDirection.Ltr) +
        other.calculateEndPadding(LayoutDirection.Ltr),
    bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
)

/**
 * Converts a single [androidx.compose.ui.unit.Dp] value into uniform
 * [androidx.compose.foundation.layout.PaddingValues] on all sides.
 *
 * This is a concise utility for applying equal padding to all edges of a layout.
 *
 * ### Example
 * ```kotlin
 * Box(modifier = Modifier.padding(16.dp.asPaddingValues()))
 * ```
 *
 * @receiver The padding amount in [androidx.compose.ui.unit.Dp] to apply to all sides.
 * @return A [androidx.compose.foundation.layout.PaddingValues] instance with equal padding on start, top, end,
 * and bottom.
 */
@Stable
fun Dp.asPaddingValues(): PaddingValues {
    return PaddingValues(this)
}
