package io.baselines.toolkit.initializer

/**
 * Represents a critical application initializer that must be executed synchronously during app startup.
 *
 * Implementations of this interface define setup logic that must complete **before** the rest of the application
 * can proceed (e.g., analytics setup, logging configuration, crash reporting, etc.).
 *
 * These initializers are executed **in a blocking manner** as part of the first step in the app launch flow.
 * If your initializer does **not** block user interaction and can safely run in the background (e.g., during
 * the splash screen), consider using [AsyncInitializer] instead to optimize startup time.
 *
 * ### When to Use
 * - The initialization is required for critical systems used immediately at launch.
 * - The app should not proceed to UI or navigation before this logic completes.
 * - The logic involves synchronous setup of core dependencies or services.
 *
 * ### Example
 * ```kotlin
 * @Inject
 * class LoggingInitializer(...) : Initializer {
 *     override suspend fun init() {
 *         // Configure Timber, Crashlytics, etc.
 *     }
 * }
 * ```
 */
interface Initializer {

    /**
     * Called by the app initialization framework to perform setup logic.
     * Implementations should complete before proceeding to the next app phase.
     */
    suspend fun init()
}
