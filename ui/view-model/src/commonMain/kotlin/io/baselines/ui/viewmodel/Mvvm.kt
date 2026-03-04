package io.baselines.ui.viewmodel

import androidx.compose.runtime.Composable

interface Mvvm<E : UiEvent, S : UiState<E>> {

    /**
     * Exposes the current UI state that should be rendered by a view (e.g., a `@Composable` function).
     *
     * Compose will automatically recompose whenever this state changes. This method should be fast and free
     * of heavy computations to avoid blocking the UI thread.
     *
     * ### Recommendation
     * If your state requires async loading, emit a lightweight placeholder first and load real data in the background.
     */
    @Composable
    fun state(): S
}
