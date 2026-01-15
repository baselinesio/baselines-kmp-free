package io.baselines.sample.compose.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import io.baselines.sample.compose.navigation.AppNavHost
import io.baselines.sample.compose.navigation.ComposeNavigator
import io.baselines.sample.ui.designsystem.components.snackbar.ComposeSnackbarHost
import io.baselines.sample.ui.designsystem.theme.AppTheme
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.toolkit.di.UiScope

@Inject
@SingleIn(UiScope::class)
class ComposeApp(
    private val metroVmFactory: MetroViewModelFactory,
    private val composeNavigator: ComposeNavigator,
    private val navGraph: Set<NavGraphEntry>,
) {

    @Composable
    operator fun invoke() {
        AppTheme {
            CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmFactory) {
                val viewModel = assistedMetroViewModel<MainViewModel, MainViewModel.Factory> {
                    create(composeNavigator, navGraph)
                }
                val state = viewModel.state()
                Scaffold(
                    snackbarHost = { ComposeSnackbarHost(hostState = state.snackbarHostState) },
                    contentWindowInsets = WindowInsets(0.dp)
                ) { padding ->
                    AnimatedContent(
                        modifier = Modifier.fillMaxSize(),
                        targetState = state.navState,
                    ) { targetNavState ->
                        if (targetNavState != null) {
                            AppNavHost(
                                modifier = Modifier
                                    .consumeWindowInsets(padding)
                                    .padding(padding),
                                state = targetNavState,
                            )
                        }
                    }
                }
            }
        }
    }
}
