package io.baselines.sample.compose.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import io.baselines.sample.compose.navigation.ComposeNavigator
import io.baselines.sample.domain.api.NavRoute
import io.baselines.sample.ui.designsystem.components.snackbar.AppSnackbarManager
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.toolkit.initializer.CompositeInitializer
import io.baselines.ui.viewmodel.BaselineViewModel
import kotlinx.collections.immutable.toImmutableSet

@AssistedInject
class MainViewModel(
    private val compositeInitializer: CompositeInitializer,
    private val snackbarManager: AppSnackbarManager,
    @Assisted private val composeNavigator: ComposeNavigator,
    @Assisted private val navGraph: Set<NavGraphEntry>,
) : BaselineViewModel<MainUiEvent, MainUiState>() {

    @Composable
    override fun state(): MainUiState {
        val appInitResult by compositeInitializer.resultFlow.collectAsStateWithLifecycle(null)
        val navState = rememberNavState(appInitResult)
        navState?.let { composeNavigator.bind(it.controller) }
        val snackbarHostState = remember { SnackbarHostState() }
        snackbarManager.bind(snackbarHostState)
        return MainUiState(
            navState = navState,
            snackbarHostState = snackbarHostState,
        ) {
            /* no-op */
        }
    }

    @Composable
    private fun rememberNavState(
        appInitResult: Result<Unit>?,
    ): MainUiState.NavStateUm? {
        val navController = rememberNavController()
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

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ManualViewModelAssistedFactory {

        fun create(
            composeNavigator: ComposeNavigator,
            navGraph: Set<NavGraphEntry>,
        ): MainViewModel
    }
}
