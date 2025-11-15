package io.baselines.ui.playground.main

import androidx.compose.runtime.Composable

@Composable
fun PlaygroundRoute(viewModel: PlaygroundViewModel) {
    val state = viewModel.state()
    val eventSink = state.eventSink
    PlaygroundScreen(
        appVersion = state.appVersion,
        loading = state.loading,
        sectionFactories = state.sectionFactories,
        searchInput = state.searchInput,
        onSearchInputChanged = { eventSink(PlaygroundUiEvent.Search(it)) }
    )
}
