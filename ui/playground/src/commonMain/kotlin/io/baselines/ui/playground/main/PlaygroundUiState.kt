package io.baselines.ui.playground.main

import androidx.compose.runtime.Immutable
import io.baselines.sample.ui.designsystem.loading.LoadingStateUm
import io.baselines.ui.playground.SectionFactory
import io.baselines.ui.viewmodel.UiState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PlaygroundUiState(
    val appVersion: String,
    val loading: LoadingStateUm?,
    val sectionFactories: ImmutableList<SectionFactory>,
    val searchInput: String,
    override val eventSink: (PlaygroundUiEvent) -> Unit,
) : UiState<PlaygroundUiEvent>
