package io.baselines.sample.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object AppNavRoutes {

    @Serializable
    data object Home : NavRoute

    @Serializable
    data object Playground : NavRoute
}
