package io.baselines.toolkit.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * A container for commonly used [kotlinx.coroutines.CoroutineDispatcher]s in the application.
 *
 * [AppDispatchers] provides a centralized way to inject and manage coroutine dispatchers across different layers
 * of the app (e.g., domain, data, presentation). This is especially useful for improving testability and
 * ensuring consistent thread usage in Kotlin Multiplatform projects.
 *
 * ### Why Use This?
 * - Decouple coroutine usage from hardcoded dispatchers (like `Dispatchers.IO`)
 * - Enable easy replacement of dispatchers in tests (e.g., with `StandardTestDispatcher`)
 * - Promote a clean architecture by passing execution context explicitly
 *
 * @property io Dispatcher optimized for offloading blocking I/O tasks (e.g., network, file, or database).
 * @property main Dispatcher intended for UI-related work (e.g., Compose recomposition or ViewModel updates).
 * @property default Dispatcher optimized for CPU-intensive tasks (e.g., sorting, parsing, or business logic).
 */
data class AppDispatchers(
    val io: CoroutineDispatcher = Dispatchers.IO,
    val main: CoroutineDispatcher = Dispatchers.Main,
    val default: CoroutineDispatcher = Dispatchers.Default,
)
