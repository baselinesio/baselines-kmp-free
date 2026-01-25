package io.baselines.ui.playground.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import io.baselines.sample.ui.designsystem.loading.LoadingController
import io.baselines.toolkit.config.AppConfigManager
import io.baselines.ui.playground.sections.PlaygroundSection
import io.baselines.ui.viewmodel.BaselineViewModel
import kotlinx.collections.immutable.toImmutableList

@AssistedInject
class PlaygroundViewModel(
    private val appConfigManager: AppConfigManager,
    @Assisted private val sections: Set<PlaygroundSection>,
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
            appVersion = appConfig.appInfo.version,
            loading = loading,
            sections = sectionFactories,
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

    @AssistedFactory
    @ContributesIntoMap(AppScope::class)
    @ManualViewModelAssistedFactoryKey(Factory::class)
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(
            sections: Set<PlaygroundSection>,
        ): PlaygroundViewModel
    }
}
