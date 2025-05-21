package io.baselines.sample.ui.designsystem.loading

import kotlinx.coroutines.flow.StateFlow

/**
 * A utility interface for managing and observing UI loading indicators in a lifecycle-aware and coroutine-friendly way.
 *
 * [LoadingController] helps coordinate the display of loading states (e.g. indeterminate spinners or
 * linear progress bars) during long-running operations triggered by the UI. It exposes a [StateFlow]
 * that emits the current [LoadingStateUm], allowing composables or views to reactively render loading indicators.
 *
 * This controller can be used directly in a [androidx.lifecycle.ViewModel] to wrap long-running operations.
 *
 * ### Example Usage in ViewModel
 * ```kotlin
 * private val loadingController = LoadingController.create()
 *
 * @Composable
 * override fun state(): PlaygroundUiState {
 *     val loading by loadingController.loading.collectAsStateWithLifecycle()
 *     return PlaygroundUiState(
 *         loading = loading,
 *         ...
 *     )
 * }
 *
 * suspend fun loadUsers(): List<User> {
 *     return loadingController.withLoadingIndication(LoadingStateUm.Indeterminate) {
 *         repository.loadUsers()
 *     }
 * }
 * ```
 */
interface LoadingController {

    /**
     * A [StateFlow] representing the current loading state.
     *
     * - Emits `null` when nothing is loading.
     * - Emits a [LoadingStateUm] instance when a loading operation is in progress.
     */
    val loading: StateFlow<LoadingStateUm?>

    /**
     * Wraps a suspending [block] with a loading indication.
     *
     * Automatically emits the given [loadingProgress] state before the operation starts,
     * and clears it once the operation completes. Supports nested loading states via an internal counter.
     *
     * @param loadingProgress The loading UI state to be shown during execution.
     * @param block The suspending function to wrap.
     * @return The result of [block].
     */
    suspend fun <T> withLoadingIndication(
        loadingProgress: LoadingStateUm,
        block: suspend () -> T
    ): T

    companion object {

        /**
         * Creates a default implementation of [LoadingController].
         *
         * You can override this to provide a custom controller in tests or feature modules.
         */
        fun create(controller: LoadingController = LoadingControllerImpl()): LoadingController = controller
    }
}
