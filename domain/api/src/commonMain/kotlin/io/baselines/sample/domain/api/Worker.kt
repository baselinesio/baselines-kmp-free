package io.baselines.sample.domain.api

import io.baselines.toolkit.logger.Logger

/**
 * A domain-layer abstraction that encapsulates business logic or use-case execution.
 *
 * [Worker] acts as a use-case-style component intended to be invoked from the UI layer or other
 * coordination layers. It provides a clear separation of concerns between presentation and domain,
 * and serves as the glue between UI and data sources, repositories, or services.
 *
 * This pattern aligns with Clean Architecture principles, promoting testability, reusability, and single responsibility.
 *
 * ### Key Features
 * - Defines a single [doWork] function to perform a unit of business logic.
 * - Provides a suspendable [invoke] operator for ergonomic usage.
 * - Wraps execution in [Result] and logs failures using [io.baselines.toolkit.logger.Logger].
 *
 * ### Usage
 * ```kotlin
 * @Inject
 * class FetchUser(
 *     private val repository: UserRepository
 * ) : Worker<UserId, User>() {
 *     override suspend fun doWork(params: UserId): User {
 *         return repository.getUserById(params)
 *     }
 * }
 *
 * // In ViewModel or other caller:
 * val result = fetchUserWorker(userId)
 * result.onSuccess { ... }.onFailure { ... }
 * ```
 *
 * @param P The type of input parameters required to perform the work.
 * @param R The type of result produced by the worker.
 */
abstract class Worker<in P, R> {

    /**
     * Invokes the worker with the given [params], wrapping the result in [Result].
     * Any exception thrown during execution will be caught and logged.
     */
    suspend operator fun invoke(params: P): Result<R> = runCatching {
        doWork(params)
    }.onFailure { Logger.e(it) { "Error while executing a domain worker." } }

    /**
     * Executes the core business logic using the provided [params].
     * Must be implemented by subclasses to perform the actual work.
     */
    protected abstract suspend fun doWork(params: P): R
}
