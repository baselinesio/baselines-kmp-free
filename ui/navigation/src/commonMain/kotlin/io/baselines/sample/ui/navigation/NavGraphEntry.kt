package io.baselines.sample.ui.navigation

import androidx.compose.runtime.Immutable

/**
 * A factory entry representing a navigation destination in the app’s navigation graph.
 *
 * [NavGraphEntry] encapsulates a [UiFactory] that declares one or more composable routes for a screen.
 * This entry is typically registered in a Dependency Injection (DI) module (e.g., Dagger/Hilt),
 * allowing the main app to discover and include feature-specific navigation destinations automatically.
 *
 * ### Why Use This?
 * - Promotes modular and decoupled navigation setup.
 * - Allows each feature module to define its own destinations.
 * - Enables scalable dynamic route registration via DI.
 *
 * ### Example: Registering a Route in a DI Module
 * ```kotlin
 * // AuthModule.kt
 * @Provides
 * @IntoSet
 * fun provideAuthNavGraphEntry(vmFactory: () -> AuthViewModel): NavGraphEntry {
 *     return NavGraphEntry { navHost ->
 *         composable<AppNavRoutes.Auth> {
 *             AuthRoute(viewModel(vmFactory))
 *         }
 *     }
 * }
 * ```
 * In this example, the `AuthRoute` screen is registered via DI and will be added to the navigation graph.
 * You can then navigate to it using `Navigator.navigate(AuthRoute)`.
 *
 * @param uiFactory A [UiFactory] that builds the composable destinations for this entry.
 */
@Immutable
data class NavGraphEntry(
    val uiFactory: UiFactory,
)
