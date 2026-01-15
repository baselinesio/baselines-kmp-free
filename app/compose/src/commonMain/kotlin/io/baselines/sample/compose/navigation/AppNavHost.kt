package io.baselines.sample.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.baselines.sample.compose.ui.MainUiState

@Composable
fun AppNavHost(
    state: MainUiState.NavStateUm,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = state.controller,
        startDestination = state.startRoute,
    ) {
        state.navGraph.forEach { entry ->
            with(entry.uiFactory) { this@NavHost.create(state.controller) }
        }
    }
}
