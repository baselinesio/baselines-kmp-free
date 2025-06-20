package io.baselines.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Base [androidx.lifecycle.ViewModel] implementation that defines a contract for handling UI [UiEvent]s
 * and exposing [UiState].
 *
 * This class provides a structure for implementing unidirectional data flow between the view layer (e.g., Compose UI)
 * and the business logic, making it easier to manage and scale your feature modules.
 *
 * ### Key Features
 * - Automatically recomposes UI when state changes.
 * - Provides a scoped coroutine launcher for background work.
 * - Offers a convenient [mutableState] builder to handle state initialization.
 *
 * @param E A subtype of [UiEvent] representing user actions or one-time UI interactions.
 * @param S A subtype of [UiState] that describes the data rendered by the view.
 */
abstract class BaselineViewModel<E : UiEvent, S : UiState<E>> : ViewModel() {

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
    abstract fun state(): S

    /**
     * Launches a coroutine in the [androidx.lifecycle.viewModelScope] using the specified [context] and [start] strategy.
     *
     * This method is useful for offloading non-UI tasks such as I/O, data processing, or network requests.
     *
     * @param context Coroutine context, defaults to [Dispatchers.Default] for background execution.
     * @param start Coroutine start strategy.
     * @param block The suspending block of code to execute.
     */
    protected fun launch(
        context: CoroutineContext = Dispatchers.Default,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        viewModelScope.launch(context, start, block)
    }

    /**
     * Creates a [MutableState] instance tied to the ViewModel’s scope, with built-in async initialization support.
     *
     * This is intended to simplify state management where state values may come from asynchronous sources
     * such as repositories or API calls.
     *
     * ### Example
     * ```kotlin
     * private val profileState = mutableState(ProfileUiState()) {
     *     loadProfileFromRepository()
     * }
     * ```
     *
     * @param initialValue The default value used immediately before the real data is loaded.
     * @param started A [SharingStarted] policy to control how long the state is kept alive (e.g. after unsubscribing).
     * @param factory A suspend function that loads the actual state value asynchronously.
     *
     * @return A [MutableState] that emits updates and can be collected by the UI.
     */
    protected fun <T> mutableState(
        initialValue: T,
        started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
        factory: (suspend () -> T)? = null,
    ): MutableState<T> {
        return MutableState(
            scope = viewModelScope,
            initialValue = initialValue,
            started = started,
            factory = factory,
        )
    }

    /**
     * Creates a [StateFlow] tied to the ViewModel’s scope, backed by a reactive data source.
     *
     * This is intended for scenarios where state updates are provided as a [Flow], such as live data streams,
     * sensor inputs, or repository-backed observables. It simplifies collecting and exposing observable
     * state to the UI layer.
     *
     * ### Example
     * ```kotlin
     * val userFlow = observableState(null) {
     *     userRepository.observeUser()
     * }
     * ```
     *
     * @param initialValue The default value emitted immediately before collecting from the [factory] flow.
     * @param started A [SharingStarted] policy that controls how long the state is active and shared.
     * @param factory A suspend function returning a [Flow] of values to be observed and emitted.
     *
     * @return A [StateFlow] that emits updates from the provided [Flow] and can be safely collected by the UI.
     */
    protected fun <T> observableState(
        initialValue: T,
        started: SharingStarted = SharingStarted.WhileSubscribed(5_000),
        factory: suspend () -> Flow<T>,
    ): StateFlow<T> {
        return flow {
            factory.invoke().collect {
                emit(it)
            }
        }.stateIn(viewModelScope, started, initialValue)
    }
}
