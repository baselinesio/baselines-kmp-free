package io.baselines.sample.ui.designsystem.loading

import androidx.compose.runtime.Immutable

/**
 * Represents the current state of a UI loading indicator.
 *
 * This sealed interface supports both indeterminate and progress-based loading visuals.
 * Use this to drive different types of indicators in your UI (e.g., circular spinner or linear bar).
 */
@Immutable
sealed interface LoadingStateUm {

    /**
     * A loading state that shows an indeterminate loading progress (e.g., never ending circular or linear animation).
     */
    @Immutable
    data object Indeterminate : LoadingStateUm

    /**
     * A loading state that shows a determinate progress bar with a percentage.
     *
     * @param progress A float from 0.0 to 1.0 indicating completion.
     */
    @Immutable
    data class Linear(val progress: Float) : LoadingStateUm
}
