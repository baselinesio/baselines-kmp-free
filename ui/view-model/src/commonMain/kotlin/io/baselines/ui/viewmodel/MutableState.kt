@file:OptIn(ExperimentalForInheritanceCoroutinesApi::class)

package io.baselines.ui.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * A state holder that wraps a [MutableStateFlow] and provides a mechanism to properly initialize the states within
 * a [androidx.lifecycle.ViewModel]. State starts initializing lazily as soon as data collection starts. This makes
 * sure you get data only when needed. Additionally, it provides control of data sharing, when should it start or
 * stop.
 *
 * ## Use Case
 * In complex screens where UI state is derived from multiple data sources (e.g., a feed and a user profile),
 * each source can be initialized independently and composed into a single UI state.
 *
 * ## How it Works
 * - **Initial value**: Used immediately to display a placeholder/default UI.
 * - **Factory**: A suspend function that produces the actual data (e.g., network, db or just runtime calculations).
 * - **State caching**: Controlled by [started] via [SharingStarted], defining how long the state is kept alive.
 * - **Threading**: The [factory] is invoked on a background dispatcher ([Dispatchers.Default]).
 * - **State updates**: Use [update] to mutate the internal state manually.
 *
 * @param scope The [CoroutineScope] in which this state is managed (usually a [androidx.lifecycle.ViewModel]'s scope).
 * @param initialValue The initial value emitted by this state before the real data is available.
 * @param factory A suspend function to load the real data asynchronously.
 * @param started A [SharingStarted] policy to control when the internal flow is active.
 */
class MutableState<T>(
    scope: CoroutineScope,
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
    private val factory: (suspend () -> T)? = null,
    private val write: MutableStateFlow<T> = MutableStateFlow(initialValue),
    read: StateFlow<T> = write
        .onStart { factory?.let { write.update { factory.invoke() } } }
        .flowOn(Dispatchers.Default)
        .stateIn(scope, started, initialValue),
) : StateFlow<T> by read {

    /**
     * Updates the internal state using a transformation function.
     * This will emit an updated state to all the subscribers.
     *
     * ## Example
     * `mutableState.update { it.copy(name = "John") }`
     *
     * @param function A transformation function to apply to the current state value.
     */
    fun update(function: (T) -> T) = write.update(function)

    /**
     * Re-runs the original [factory] and replaces the current state with the newly produced value.
     * Useful when external actions change the underlying data source and the cached state needs to
     * be refreshed without recreating the whole view model.
     *
     * ## Example
     * `mutableState.recreate()`
     */
    suspend fun recreate() {
        factory?.invoke()?.let { recreatedState ->
            write.update { recreatedState }
        }
    }
}
