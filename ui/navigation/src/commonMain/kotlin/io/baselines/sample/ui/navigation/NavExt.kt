package io.baselines.sample.ui.navigation

/**
 * A helper function that returns a [RouteNavOptionsBuilder] to open a new route while clearing the back stack.
 *
 * This is commonly used in navigation scenarios where the current navigation history should be cleared —
 * for example, after login, logout, or onboarding — and the user should not be able to navigate back
 * to previous screens.
 *
 * Internally, it uses `popUpTo` with the navigation graph’s start destination and applies `launchSingleTop`
 * to prevent multiple instances of the same destination.
 *
 * @param inclusive If `true`, the start destination itself will also be removed from the back stack.
 * @return A [RouteNavOptionsBuilder] that can be passed to [Navigator.navigate] or similar APIs.
 *
 * ### Example
 * ```kotlin
 * navigator.navigate(HomeRoute, clearTopNavOptions())
 * ```
 */
fun clearTopNavOptions(inclusive: Boolean = true) = RouteNavOptionsBuilder { graph ->
    launchSingleTop = true
    popUpTo(0) { this.inclusive = inclusive }
}
