package io.baselines.sample.ui.navigation

import kotlinx.coroutines.flow.Flow

/**
 * A navigation handler interface responsible for managing navigation requests across the app.
 *
 * Implementations of [Navigator] are typically used by ViewModels or UI layers to trigger navigation events
 * in a decoupled and testable way. It emits [NavEvent]s that are observed and consumed by a navigation host
 * (e.g., using [androidx.navigation.NavHostController]).
 *
 * This abstraction allows navigation logic to remain outside of composables, promoting clean architecture principles.
 */
interface Navigator {

    /**
     * A hot [Flow] that emits [NavEvent]s representing navigation actions such as forward navigation,
     * back navigation, or up navigation. Typically collected by a host that owns the NavController.
     */
    val events: Flow<NavEvent>

    /**
     * Requests navigation to the specified [route]. Optionally allows custom navigation options such as pop-up
     * behavior, animations, or launch flags through [navOptionsBuilder].
     *
     * @param route The [NavRoute] destination to navigate to.
     * @param navOptionsBuilder Optional builder to customize navigation behavior.
     */
    fun navigate(
        route: NavRoute,
        navOptionsBuilder: RouteNavOptionsBuilder? = null,
    )

    /**
     * Navigates back in the navigation stack. If a specific [route] is provided, the back stack is popped
     * up to that destination. The [inclusive] flag determines whether the [route] itself should also be removed.
     *
     * @param route The target route to pop back to. If null, performs a regular back stack pop.
     * @param inclusive Whether the target [route] should also be removed from the back stack.
     */
    fun navigateBack(route: NavRoute? = null, inclusive: Boolean = false)

    /**
     * Navigates up in the navigation hierarchy, mimicking the behavior of a system back or toolbar up button.
     */
    fun navigateUp()
}
