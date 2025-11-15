package io.baselines.sample.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.sample.ui.navigation.NavRoute
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun AppNavHost(
    navController: NavHostController,
    navGraph: ImmutableSet<NavGraphEntry>,
    startRoute: NavRoute,
) {
    NavHost(navController, startRoute) {
        navGraph.forEach { entry ->
            with(entry.uiFactory) { this@NavHost.create(navController) }
        }
    }
}
