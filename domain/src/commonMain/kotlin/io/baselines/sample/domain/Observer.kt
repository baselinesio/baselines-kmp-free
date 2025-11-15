package io.baselines.sample.domain

import kotlinx.coroutines.flow.Flow

/**
 * A domain-layer abstraction that provides a reactive stream (Flow) to observe business data or state over time.
 *
 * [Observer] acts as a use-case-style component intended to be used by the UI layer or other coordination layers
 * when continuous updates are required, such as observing live data streams, state changes, or remote updates.
 *
 * This pattern complements [Worker] by supporting observable flows rather than single-shot operations.
 * It encourages a clean separation of concerns and aligns with Clean Architecture principles.
 *
 * ### Key Features
 * - Defines a single [create] function that returns a [Flow] of results.
 * - Provides a suspendable [invoke] operator for ergonomic integration.
 * - Designed for scenarios where data must be observed reactively over time.
 *
 * ### Usage
 * ```kotlin
 * @Inject
 * class ObserveUserStream(
 *     private val repository: UserRepository
 * ) : Observer<UserId, User>() {
 *     override suspend fun create(params: UserId): Flow<User> {
 *         return repository.observeUserById(params)
 *     }
 * }
 *
 * // In ViewModel or other caller:
 * viewModelScope.launch {
 *     observeUserStream(userId).collect { user ->
 *         // react to updates
 *     }
 * }
 * ```
 *
 * @param P The type of input parameters used to initiate the observation.
 * @param R The type of values emitted by the resulting [Flow].
 */
abstract class Observer<in P, R> {

    /**
     * Convenience operator that delegates to [create], allowing the [Observer] to be invoked
     * like a regular function.
     */
    suspend operator fun invoke(params: P): Flow<R> = create(params)

    /**
     * Subclasses must implement this method to define the reactive stream of values
     * to be observed for the given [params].
     */
    protected abstract suspend fun create(params: P): Flow<R>
}
