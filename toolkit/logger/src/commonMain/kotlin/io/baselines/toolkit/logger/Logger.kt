package io.baselines.toolkit.logger

import io.baselines.toolkit.logger.Logger.writer


/**
 * Singleton logging API exposed to the application and libraries.
 *
 * [Logger] acts as the centralized entry point for logging across a KMP project.
 * It delegates logging calls to a [LogWriter] implementation (e.g., [KermitWriter]).
 *
 * All log messages are passed as lambdas to avoid unnecessary string creation unless the message is actually logged.
 *
 * ### Configuration
 * Before using the logger, you must assign a [LogWriter] implementation to [writer], typically during app startup:
 *
 * ```kotlin
 * Logger.writer = KermitWriter()
 * ```
 *
 * ### Usage
 * ```kotlin
 * Logger.i { "App started" }
 * Logger.d { "User ID: $userId" }
 * Logger.e(exception) { "Something went wrong" }
 * ```
 */
object Logger {

    var writer: LogWriter? = null

    /**
     * Logs an info-level message.
     */
    fun i(message: () -> String) = writer?.i(message)

    /**
     * Logs a debug-level message.
     */
    fun d(message: () -> String) = writer?.d(message)

    /**
     * Logs an error-level message with optional exception.
     */
    fun e(throwable: Throwable? = null, message: () -> String) = writer?.e(throwable, message)
}
