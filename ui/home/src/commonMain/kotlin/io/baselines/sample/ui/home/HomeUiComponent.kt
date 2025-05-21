package io.baselines.sample.ui.home

import androidx.navigation.compose.composable
import io.baselines.sample.ui.navigation.AppNavRoutes
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.toolkit.di.UiScope
import io.baselines.ui.viewmodel.viewModel
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(UiScope::class)
interface HomeUiComponent {

    @Provides
    @IntoSet
    fun provideHomeNavGraphEntry(
        vmFactory: () -> HomeViewModel,
    ): NavGraphEntry {
        return NavGraphEntry {
            composable<AppNavRoutes.Home> {
                HomeRoute(viewModel(vmFactory))
            }
        }
    }
}
