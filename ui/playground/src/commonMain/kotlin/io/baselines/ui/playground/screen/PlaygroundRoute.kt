package io.baselines.ui.playground.screen

import androidx.compose.runtime.Composable

@Composable
fun PlaygroundRoute(viewModel: PlaygroundViewModel) {
    val state = viewModel.state()
    val eventSink = state.eventSink
    PlaygroundScreen(
        appVersion = state.appVersion,
        loading = state.loading,
        sections = state.sections,
        searchInput = state.searchInput,
        onSearchInputChanged = { eventSink(PlaygroundUiEvent.Search(it)) }
    )
}
