package io.baselines.ui.playground.di

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import io.baselines.sample.ui.navigation.AppNavRoutes
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.toolkit.di.UiScope
import io.baselines.ui.playground.screen.PlaygroundRoute
import io.baselines.ui.playground.screen.PlaygroundViewModel
import io.baselines.ui.playground.sections.PlaygroundSection
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(UiScope::class)
interface PlaygroundUiComponent {

    @Provides
    @IntoSet
    fun providePlaygroundNavGraphEntry(
        vmFactory: (Set<PlaygroundSection>) -> PlaygroundViewModel,
        sections: Set<PlaygroundSection>,
    ): NavGraphEntry {
        return NavGraphEntry {
            composable<AppNavRoutes.Playground> {
                PlaygroundRoute(viewModel { vmFactory(sections) })
            }
        }
    }
}
