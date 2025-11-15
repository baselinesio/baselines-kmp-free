package io.baselines.sample.compose.initializer

import io.baselines.sample.ui.navigation.AppNavRoutes
import io.baselines.sample.ui.navigation.NavRoute
import io.baselines.toolkit.initializer.AsyncInitializer
import me.tatarka.inject.annotations.Inject

@Inject
class StartRouteInitializer : AsyncInitializer {

    override suspend fun init() {
        NavRoute.Default = AppNavRoutes.Home
    }
}
