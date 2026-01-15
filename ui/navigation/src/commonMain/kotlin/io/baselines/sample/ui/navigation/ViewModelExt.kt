package io.baselines.sample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import dev.zacsweers.metrox.viewmodel.metroViewModel

/**
 * Retrieves a [ViewModel] instance that is scoped to the **parent navigation graph** of the
 * current destination, rather than the destination itself.
 *
 * This allows multiple screens within a nested navigation graph to share the same ViewModel instance.
 * The ViewModel will be retained as long as the parent navigation graph is on the back stack
 * and will be cleared when the graph is popped.
 *
 * @param T The type of the ViewModel to retrieve. Must be a subclass of [ViewModel].
 * @param navController The [NavHostController] used to look up the parent back stack entry.
 * @return An instance of [T]. If the current destination has a parent graph with a valid route,
 * the instance is scoped to that graph. Otherwise, it falls back to creating/retrieving a
 * ViewModel scoped to the current destination.
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedMetroViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return metroViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return metroViewModel(parentEntry)
}
