package io.baselines.sample.compose.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.baselines.sample.compose.navigation.AppNavHost
import io.baselines.sample.ui.designsystem.theme.AppTheme
import io.baselines.toolkit.di.UiScope
import io.baselines.ui.viewmodel.viewModel
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(UiScope::class)
class ComposeApp(private val viewModelFactory: () -> MainViewModel) {

    @Composable
    operator fun invoke() {
        AppTheme {
            val viewModel = viewModel(viewModelFactory)
            val state = viewModel.state()
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = state.navState,
            ) { targetNavState ->
                if (targetNavState != null) {
                    AppNavHost(
                        navController = targetNavState.controller,
                        navGraph = targetNavState.navGraph,
                        startRoute = targetNavState.startRoute,
                    )
                }
            }
        }
    }
}
