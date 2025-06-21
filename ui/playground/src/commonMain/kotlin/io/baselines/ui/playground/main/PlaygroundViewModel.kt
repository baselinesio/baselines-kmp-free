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
    @Assisted private val sections: Set<SectionFactory>,
) : BaselineViewModel<PlaygroundUiEvent, PlaygroundUiState>() {

    private val searchInputFlow = mutableState("")
    private val sectionFactoriesFlow = mutableState(sections.toImmutableList())
    private val loadingController = LoadingController.create()

    @Composable
    override fun state(): PlaygroundUiState {
        val loading by loadingController.loading.collectAsStateWithLifecycle()
        val appConfig by appConfigManager.appConfig.collectAsStateWithLifecycle()
        val searchInput by searchInputFlow.collectAsStateWithLifecycle()
        val sectionFactories by sectionFactoriesFlow.collectAsStateWithLifecycle()
        return PlaygroundUiState(
            appVersion = appConfig.version,
            loading = loading,
            sectionFactories = sectionFactories,
            searchInput = searchInput,
        ) { event ->
            when (event) {
                is PlaygroundUiEvent.Search -> handleSearch(event.input)
            }
        }
    }

    private fun handleSearch(input: String) {
        searchInputFlow.update { input }
        launch {
            val sections = sections.filter { section ->
                section.name.contains(input, ignoreCase = true)
            }.toImmutableList()
            sectionFactoriesFlow.update { sections }
        }
    }
}
