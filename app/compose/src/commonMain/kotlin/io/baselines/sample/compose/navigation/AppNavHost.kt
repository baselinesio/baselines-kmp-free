package io.baselines.sample.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import io.baselines.sample.compose.ui.MainUiState

@Composable
fun AppNavHost(
    state: MainUiState.NavStateUm,
) {
    NavHost(state.controller, state.startRoute) {
        state.navGraph.forEach { entry ->
            with(entry.uiFactory) { this@NavHost.create(state.controller) }
        }
    }
}
