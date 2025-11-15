package io.baselines.sample.ui.home

import androidx.compose.runtime.Immutable
import io.baselines.ui.viewmodel.UiState

@Immutable
data class HomeUiState(
    override val eventSink: (HomeUiEvent) -> Unit,
) : UiState<HomeUiEvent>
