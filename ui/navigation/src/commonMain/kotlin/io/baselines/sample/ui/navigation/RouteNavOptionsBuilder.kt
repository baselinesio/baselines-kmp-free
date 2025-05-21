package io.baselines.sample.ui.navigation

import androidx.navigation.NavGraph
import androidx.navigation.NavOptionsBuilder

/**
 * A builder interface for configuring navigation behavior when navigating to a destination.
 *
 * [RouteNavOptionsBuilder] allows you to customize how navigation entries are opened by modifying
 * the [androidx.navigation.NavOptionsBuilder]. This includes configuring pop-up behavior, animations, single top flags,
 * and more.
 *
 * This is particularly useful for feature modules that want to define custom navigation transitions or back stack rules
 * in a centralized and reusable way.
 *
 * ### Example Use Case
 * - Popping the current destination from the back stack before navigating.
 * - Applying enter/exit animations.
 * - Enforcing singleTop navigation.
 *
 * ### Example
 * ```kotlin
 * RouteNavOptionsBuilder { graph ->
 *     popUpTo(graph.startDestinationRoute) {
 *         inclusive = true
 *     }
 *     launchSingleTop = true
 * }
 * ```
 */
fun interface RouteNavOptionsBuilder {

    /**
     * Configures the [androidx.navigation.NavOptionsBuilder] with the desired navigation behavior.
     *
     * @param graph The [androidx.navigation.NavGraph] in which the current navigation is taking place.
     * Useful for referencing destinations or IDs when applying options such as `popUpTo`.
     */
    fun NavOptionsBuilder.configure(graph: NavGraph)
}
