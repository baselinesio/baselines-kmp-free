package io.baselines.sample.ui.navigation

import androidx.compose.runtime.Stable
import io.baselines.sample.ui.navigation.NavRoute.Companion.Default
import kotlinx.serialization.Serializable

/**
 * Represents a navigation destination in the application.
 *
 * Each [NavRoute] defines a unique route that should correspond to a `@Composable` screen in the app.
 * This interface is sealed and serializable, allowing safe and predictable navigation using type-safe routes.
 *
 * ### Usage
 * Each route needs to be defined in [AppNavRoutes] by implementing this interface. For example:
 *
 * ```kotlin
 * @Serializable
 * object AppNavRoutes {
 *
 *     @Serializable
 *     data object Playground : NavRoute
 * }
 * ```
 *
 * The navigation system will use the `NavRoute` to match a corresponding composable screen via the route registry.
 *
 * ### Default Route
 * The [Default] property can be used to specify the entry point or fallback screen of the app.
 */
@Serializable
@Stable
sealed interface NavRoute {

    companion object {

        /**
         * The default route used when the app starts.
         */
        var Default: NavRoute = AppNavRoutes.Home
    }
}
