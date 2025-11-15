package io.baselines.sample.ui.home

import androidx.compose.runtime.Composable
import io.baselines.sample.ui.navigation.AppNavRoutes
import io.baselines.sample.ui.navigation.Navigator
import io.baselines.ui.viewmodel.BaselineViewModel
import me.tatarka.inject.annotations.Inject

@Inject
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
