package io.baselines.sample.ui.home

import androidx.navigation.compose.composable
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.baselines.sample.domain.api.AppNavRoutes
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.toolkit.di.UiScope

@ContributesTo(UiScope::class)
interface HomeUiModule {

    @Provides
    @IntoSet
    fun provideHomeNavGraphEntry(): NavGraphEntry {
        return NavGraphEntry {
            composable<AppNavRoutes.Home> {
                HomeRoute(metroViewModel())
            }
        }
    }
}
