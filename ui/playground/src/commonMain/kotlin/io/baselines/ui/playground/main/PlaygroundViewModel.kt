package io.baselines.ui.playground.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.baselines.sample.ui.designsystem.loading.LoadingController
import io.baselines.toolkit.config.AppConfigManager
import io.baselines.ui.viewmodel.BaselineViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.tatarka.inject.annotations.Inject

@Inject
class PlaygroundViewModel(
    private val appConfigManager: AppConfigManager,
) : BaselineViewModel<PlaygroundUiEvent, PlaygroundUiState>() {

    private val sectionsFlow = mutableState(persistentListOf()) { createSections() }
    private val loadingController = LoadingController.create()

    @Composable
    override fun state(): PlaygroundUiState {
        val loading by loadingController.loading.collectAsStateWithLifecycle()
        val appConfig by appConfigManager.appConfig.collectAsStateWithLifecycle()
        val sections by sectionsFlow.collectAsStateWithLifecycle()
        return PlaygroundUiState(
            appVersion = appConfig.version,
            loading = loading,
            sections = sections,
        ) { _ ->
            /* no-op */
        }
    }

    private fun createSections(): ImmutableList<PlaygroundUiState.SectionUm> {
        return persistentListOf(
            PlaygroundUiState.SectionUm.Typography,
            PlaygroundUiState.SectionUm.Spacings,
        )
    }
}
