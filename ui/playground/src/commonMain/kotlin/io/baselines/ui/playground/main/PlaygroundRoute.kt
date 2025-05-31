package io.baselines.ui.playground.main

import androidx.compose.runtime.Composable

@Composable
fun PlaygroundRoute(viewModel: PlaygroundViewModel) {
    val state = viewModel.state()
    PlaygroundScreen(
        appVersion = state.appVersion,
        loading = state.loading,
        sectionFactories = state.sectionFactories,
    )
}
