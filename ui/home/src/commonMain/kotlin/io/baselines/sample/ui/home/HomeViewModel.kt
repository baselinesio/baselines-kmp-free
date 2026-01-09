package io.baselines.sample.ui.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import io.baselines.sample.domain.api.AppNavRoutes
import io.baselines.sample.ui.navigation.Navigator
import io.baselines.ui.viewmodel.BaselineViewModel

@Inject
@ContributesIntoMap(
    scope = AppScope::class,
    binding = binding<ViewModel>()
)
@ViewModelKey(HomeViewModel::class)
class HomeViewModel(
    private val navigator: Navigator,
) : BaselineViewModel<HomeUiEvent, HomeUiState>() {

    @Composable
    override fun state(): HomeUiState {
        return HomeUiState { event ->
            when (event) {
                HomeUiEvent.OpenPlayground -> navigator.navigate(AppNavRoutes.Playground)
            }
        }
    }
}
