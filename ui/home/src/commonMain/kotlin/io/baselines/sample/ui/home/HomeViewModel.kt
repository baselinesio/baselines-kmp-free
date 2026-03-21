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
import io.baselines.ui.viewmodel.Mvvm
import io.baselines.ui.viewmodel.createEventSink

@Inject
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
@ViewModelKey(HomeViewModel::class)
class HomeViewModel(
    private val navigator: Navigator,
) : ViewModel(), Mvvm<HomeUiEvent, HomeUiState> {

    private val eventSink = createEventSink(::handleEvent)

    @Composable
    override fun state(): HomeUiState {
        return HomeUiState(
            eventSink = eventSink,
        )
    }

    private fun handleEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OpenPlayground -> navigator.navigate(AppNavRoutes.Playground)
        }
    }
}
