package io.baselines.ui.playground.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.baselines.sample.ui.designsystem.loading.LoadingController
import io.baselines.toolkit.config.AppConfigManager
import io.baselines.ui.playground.SectionFactory
import io.baselines.ui.viewmodel.BaselineViewModel
import kotlinx.collections.immutable.toImmutableList
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class PlaygroundViewModel(
    private val appConfigManager: AppConfigManager,
    @Assisted sections: Set<SectionFactory>,
) : BaselineViewModel<PlaygroundUiEvent, PlaygroundUiState>() {

    private val sectionFactories = sections.toImmutableList()
    private val loadingController = LoadingController.create()

    @Composable
    override fun state(): PlaygroundUiState {
        val loading by loadingController.loading.collectAsStateWithLifecycle()
        val appConfig by appConfigManager.appConfig.collectAsStateWithLifecycle()
        return PlaygroundUiState(
            appVersion = appConfig.version,
            loading = loading,
            sectionFactories = sectionFactories,
        ) { _ ->
            /* no-op */
        }
    }
}
