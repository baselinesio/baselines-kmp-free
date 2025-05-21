package io.baselines.toolkit.logger

/**
 * Interface that defines the contract for writing log messages.
 *
 * Implementations are responsible for routing log calls (info, debug, error) to a desired logging backend.
 * Each log message is passed as a lambda to support lazy initialization — ensuring the string is only evaluated
 * if the log level is enabled.
 */
interface LogWriter {

    /**
     * Logs an informational message (e.g., app lifecycle, state changes).
     */
    fun i(message: () -> String)

    /**
     * Logs a debug message (e.g., internal flow, variable values).
     */
    fun d(message: () -> String)

    /**
     * Logs an error message with optional [Throwable] (e.g., failures, exceptions).
     */
    fun e(throwable: Throwable? = null, message: () -> String)
}
