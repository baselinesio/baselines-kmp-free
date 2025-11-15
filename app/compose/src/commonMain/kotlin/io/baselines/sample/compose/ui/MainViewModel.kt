package io.baselines.sample.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.baselines.sample.compose.navigation.ComposeNavigator
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.sample.ui.navigation.NavRoute
import io.baselines.toolkit.initializer.CompositeInitializer
import io.baselines.ui.viewmodel.BaselineViewModel
import kotlinx.collections.immutable.toImmutableSet
import me.tatarka.inject.annotations.Inject

@Inject
class MainViewModel(
    private val compositeInitializer: CompositeInitializer,
    private val composeNavigator: ComposeNavigator,
    private val navGraph: Set<NavGraphEntry>,
) : BaselineViewModel<MainUiEvent, MainUiState>() {

    @Composable
    override fun state(): MainUiState {
        val appInitResult by compositeInitializer.resultFlow.collectAsStateWithLifecycle(null)
        val navController = rememberNavController()
        composeNavigator.bind(navController)
        val navState = rememberNavState(appInitResult, navController)
        return MainUiState(
            navState = navState
        ) {
            /* no-op */
        }
    }

    @Composable
    private fun rememberNavState(
        appInitResult: Result<Unit>?,
        navController: NavHostController,
    ): MainUiState.NavStateUm? {
        return remember(appInitResult) {
            appInitResult?.let {
                if (it.isSuccess) {
                    MainUiState.NavStateUm(
                        controller = navController,
                        navGraph = navGraph.toImmutableSet(),
                        startRoute = NavRoute.Default,
                    )
                } else {
                    null
                }
            }
        }
    }
}
