package io.baselines.ui.playground.di

import androidx.navigation.compose.composable
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import io.baselines.sample.domain.api.AppNavRoutes
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.toolkit.di.UiScope
import io.baselines.ui.playground.screen.PlaygroundRoute
import io.baselines.ui.playground.screen.PlaygroundViewModel
import io.baselines.ui.playground.sections.PlaygroundSection

@ContributesTo(UiScope::class)
interface PlaygroundUiModule {

    @Provides
    @IntoSet
    fun providePlaygroundNavGraphEntry(sections: Set<PlaygroundSection>): NavGraphEntry {
        return NavGraphEntry {
            composable<AppNavRoutes.Playground> {
                val viewModel = assistedMetroViewModel<PlaygroundViewModel, PlaygroundViewModel.Factory> {
                    create(sections)
                }
                PlaygroundRoute(viewModel)
            }
        }
    }
}
