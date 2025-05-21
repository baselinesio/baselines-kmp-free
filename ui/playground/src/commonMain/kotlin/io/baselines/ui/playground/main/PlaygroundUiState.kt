package io.baselines.ui.playground.main

import androidx.compose.runtime.Immutable
import io.baselines.sample.ui.designsystem.loading.LoadingStateUm
import io.baselines.ui.viewmodel.UiState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PlaygroundUiState(
    val appVersion: String,
    val loading: LoadingStateUm?,
    val sections: ImmutableList<SectionUm>,
    override val eventSink: (PlaygroundUiEvent) -> Unit,
) : UiState<PlaygroundUiEvent> {

    sealed class SectionUm(val title: String) {

        data object Typography : SectionUm("Typography")

        data object Spacings : SectionUm("Spacings")
    }
}
