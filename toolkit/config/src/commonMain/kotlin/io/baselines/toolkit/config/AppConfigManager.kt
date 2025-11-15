package io.baselines.toolkit.config

import kotlinx.coroutines.flow.StateFlow

/**
 * A contract for managing and observing the application's configuration state.
 *
 * [AppConfigManager] provides a centralized mechanism to store, observe, and update app-wide settings
 * such as feature flags, user preferences, environment configuration, or other runtime options.
 *
 * The configuration is exposed as a [kotlinx.coroutines.flow.StateFlow], allowing reactive observation
 * from anywhere in the app. The current configuration is always immediately available and any updates will
 * automatically emit the latest version to all collectors.
 */
interface AppConfigManager {

    /**
     * A [kotlinx.coroutines.flow.StateFlow] that emits the latest [AppConfig].
     *
     * - Emits the current configuration immediately upon collection.
     * - Emits new values whenever [update] is called with a modified config.
     * - If the updated config is equal to the current one, no emission occurs.
     *
     * This flow does not operate on a specific dispatcher—collect on an appropriate context if needed.
     */
    val appConfig: StateFlow<AppConfig>

    /**
     * Returns the current [AppConfig] without requiring flow collection.
     *
     * This is a convenient way to synchronously access the latest state of the configuration.
     */
    val current: AppConfig
        get() = appConfig.value

    /**
     * Updates the application configuration using the provided [updater] function.
     *
     * If the resulting config is different from the current one, it is emitted via [appConfig].
     * Otherwise, no change is emitted.
     *
     * @param updater A function that receives the current config and returns a new updated config.
     *
     * ### Example
     * ```kotlin
     * appConfigManager.update { current ->
     *     current.copy(debug = true)
     * }
     * ```
     */
    fun update(updater: (AppConfig) -> AppConfig)
}
