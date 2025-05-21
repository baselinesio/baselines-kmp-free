package io.baselines.sample.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.sample.ui.navigation.NavRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    navGraph: Set<NavGraphEntry>,
    startRoute: NavRoute,
) {
    NavHost(navController, startRoute) {
        navGraph.forEach { entry ->
            with(entry.uiFactory) { this@NavHost.create(navController) }
        }
    }
}
