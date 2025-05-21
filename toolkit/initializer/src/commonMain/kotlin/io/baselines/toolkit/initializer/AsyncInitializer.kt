package io.baselines.toolkit.initializer

/**
 * Represents a non-blocking application initializer that runs asynchronously during the app startup flow.
 *
 * [AsyncInitializer] is designed for setup tasks that are **not critical to block the main launch flow** and
 * can safely execute in the background—typically while a splash screen is shown.
 *
 * This interface extends [Initializer], but its implementations are scheduled to run **concurrently** with
 * other async initializers and after the critical [Initializer]s have completed.
 *
 * ### When to Use
 * - The initialization task does not affect core functionality or navigation.
 * - The logic is safe to perform while the splash screen or loading UI is visible.
 * - The setup improves UX or analytics but is not required immediately.
 *
 * ### Example
 * ```kotlin
 * @Inject
 * class RemoteConfigInitializer constructor(...) : AsyncInitializer {
 *     override suspend fun init() {
 *         // Fetch remote config, pre-warm caches, etc.
 *     }
 * }
 * ```
 *
 * @see Initializer for blocking startup tasks.
 */
interface AsyncInitializer : Initializer
