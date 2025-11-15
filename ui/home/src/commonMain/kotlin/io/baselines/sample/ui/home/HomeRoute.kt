package io.baselines.sample.ui.home

import androidx.compose.runtime.Composable

@Composable
fun HomeRoute(viewModel: HomeViewModel) {
    val state = viewModel.state()
    val eventSink = state.eventSink
    HomeScreen(
        onOpenPlaygroundClicked = { eventSink(HomeUiEvent.OpenPlayground) }
    )
}
