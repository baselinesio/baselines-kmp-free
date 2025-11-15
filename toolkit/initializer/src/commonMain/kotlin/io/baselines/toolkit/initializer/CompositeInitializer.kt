package io.baselines.toolkit.initializer

import io.baselines.toolkit.coroutines.AppDispatchers
import io.baselines.toolkit.logger.Logger
import kotlin.time.TimeSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

/**
 * Orchestrates application startup by coordinating execution of both core (blocking) and async (non-blocking) initializers.
 *
 * [CompositeInitializer] wraps all registered [Initializer]s and [AsyncInitializer]s, ensuring they are executed in
 * the correct order during the app’s launch flow.
 *
 * ### Initialization Order:
 * 1. Executes all [Initializer]s **synchronously on the main thread**. These are critical to app launch.
 * 2. If core initialization succeeds, proceeds to execute [AsyncInitializer]s **in the background**.
 * 3. Emits a combined [Result] through [resultFlow] to signal the outcome of initialization.
 *
 * This class is typically invoked from the application’s main entry point (e.g., during splash or launcher flow).
 *
 * @param asyncInitializersLazy Non-blocking initializers to be run after the core ones.
 * @param coreInitializersLazy Blocking initializers that must complete before the app proceeds.
 * @param appDispatchers App-specific coroutine dispatchers for managing thread context.
 */
@Inject
@SingleIn(AppScope::class)
class CompositeInitializer(
    private val asyncInitializersLazy: Set<Lazy<AsyncInitializer>>,
    private val coreInitializersLazy: Set<Lazy<Initializer>>,
    private val appDispatchers: AppDispatchers,
) {
    private val resultStateFlow = MutableSharedFlow<Result<Unit>>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val scope: CoroutineScope = CoroutineScope(appDispatchers.main)


    /**
     * A [Flow] that emits the result of the entire app initialization process.
     *
     * - Emits a [Result.success] when all initializers complete successfully.
     * - Emits a [Result.failure] if one or more initializers throw exceptions.
     * - Replay count is 1, so consumers can collect this even after initialization finishes.
     */
    val resultFlow: SharedFlow<Result<Unit>> = resultStateFlow.asSharedFlow()

    /**
     * Starts the app initialization process.
     *
     * 1. Launches all [Initializer]s on the **main thread**, in order.
     * 2. If all succeed, continues with [AsyncInitializer]s on the **default dispatcher**.
     * 3. Emits the final result via [resultFlow].
     */
    fun initialize() {
        scope.launch {
            val coreInitResult = executeInit(coreInitializersLazy)
            if (coreInitResult.isSuccess) {
                withContext(appDispatchers.default) {
                    val asyncInitResult = executeInit(asyncInitializersLazy)
                    resultStateFlow.emit(asyncInitResult)
                }
            } else {
                resultStateFlow.emit(coreInitResult)
            }
        }
    }

    private suspend fun executeInit(initializersLazy: Set<Lazy<Initializer>>): Result<Unit> {
        val results = mutableListOf<Result<Unit>>()
        for (initializerLazy in initializersLazy) {
            val initializer by initializerLazy
            val initStartTimeMark = TimeSource.Monotonic.markNow()
            val result = runCatching { initializer.init() }
            val initDurationMillis = initStartTimeMark.elapsedNow().inWholeMilliseconds
            val logMessageInitResult = "${initializer::class.simpleName}: ${result::class.simpleName}"
            val logMessageInitDuration = "duration = $initDurationMillis ms"
            Logger.i { "$logMessageInitResult; $logMessageInitDuration" }
            results.add(result)
        }
        return createCompositeResult(results)
    }

    private fun createCompositeResult(results: List<Result<Unit>>): Result<Unit> {
        val failures = results.filter { it.isFailure }
        return if (failures.isEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(CompositeInitializerException(failures))
        }
    }

    /**
     * Represents a combined failure when one or more initializers fail.
     *
     * All individual initializer exceptions are attached via [Throwable.addSuppressed] to provide complete
     * visibility into what went wrong during startup.
     *
     * @param failures A list of failed [Result]s from initializers.
     * @param message An optional error message describing the failure context.
     */
    class CompositeInitializerException(
        failures: List<Result<Unit>>,
        message: String = "Failure while executing some of the registered initializers.",
    ) : Exception(message) {

        init {
            failures.forEach {
                addSuppressed(requireNotNull(it.exceptionOrNull()))
            }
        }
    }
}
