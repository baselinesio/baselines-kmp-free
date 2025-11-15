@file:OptIn(ExperimentalUuidApi::class)

package io.baselines.sample.ui.navigation

import androidx.compose.runtime.Immutable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a navigation action that can be emitted by a [Navigator] and consumed by the navigation host.
 *
 * This sealed interface defines all supported types of navigation events in the app, such as navigating
 * to a route, popping the back stack, or navigating up in the hierarchy. Each event carries a unique [id]
 * to support scenarios like distinguishing or de-duplicating repeated events.
 *
 * All events are marked as [@Immutable] to ensure they work safely with Compose and other state holders.
 */
@Immutable
sealed interface NavEvent {

    /**
     * A unique identifier for the navigation event.
     * This helps ensure that duplicate events are not processed more than once.
     */
    val id: String

    /**
     * A placeholder or no-op event. Useful as a default or when no navigation is required.
     */
    @Immutable
    data class Undefined(
        override val id: String = Uuid.random().toString(),
    ) : NavEvent

    /**
     * Represents a forward navigation to a specific [NavRoute], optionally customized
     * with a [RouteNavOptionsBuilder] (e.g., for animations or stack behavior).
     *
     * @param route The destination route to navigate to.
     * @param optionsBuilder Optional navigation options (e.g., popUpTo, singleTop).
     * @param id Unique identifier for the event.
     */
    @Immutable
    data class Navigate(
        val route: NavRoute,
        val optionsBuilder: RouteNavOptionsBuilder?,
        override val id: String = Uuid.random().toString(),
    ) : NavEvent

    /**
     * Represents a request to navigate back in the navigation stack.
     * Optionally allows popping up to a specific [route] and specifying whether it should be removed too.
     *
     * @param route The route to pop back to. If null, performs a simple back stack pop.
     * @param inclusive Whether the target [route] should also be removed.
     * @param id Unique identifier for the event.
     */
    @Immutable
    data class Back(
        val route: NavRoute? = null,
        val inclusive: Boolean = false,
        override val id: String = Uuid.random().toString(),
    ) : NavEvent

    /**
     * Represents a request to navigate "up" in the navigation hierarchy.
     * Typically used with nested graphs or when mimicking the system back button behavior.
     *
     * @param id Unique identifier for the event.
     */
    @Immutable
    data class Up(
        override val id: String = Uuid.random().toString(),
    ) : NavEvent
}
