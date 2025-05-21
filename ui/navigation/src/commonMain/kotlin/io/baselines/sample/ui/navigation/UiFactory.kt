package io.baselines.sample.ui.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * A factory interface used by [NavGraphEntry] to define UI destinations.
 *
 * This factory is responsible for registering `composable` routes inside a [androidx.navigation.NavGraphBuilder],
 * which are then consumed by [androidx.navigation.NavHost]. The purpose of [UiFactory] is to encapsulate
 * the creation of navigation destinations within a feature module, promoting modularity and keeping
 * feature-specific navigation decoupled from the main application.
 *
 * ### Why Use This?
 * - Keeps destinations localized to their feature modules.
 * - Encourages modular architecture and separation of concerns.
 * - Simplifies the main navigation graph by delegating destination setup.
 *
 * @see NavGraphEntry
 */
@Stable
fun interface UiFactory {

    /**
     * Defines a UI destination within the [androidx.navigation.NavGraphBuilder].
     *
     * This method should use the `composable` extension to declare one or more destinations,
     * using [navHost] if needed.
     *
     * @param navHost The [androidx.navigation.NavHostController] that manages navigation for this graph.
     */
    fun NavGraphBuilder.create(navHost: NavHostController)
}
