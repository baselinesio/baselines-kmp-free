package io.baselines.sample.domain.api

import kotlinx.serialization.Serializable

/**
 * Namespace for an easy access to all the app nav routes.
 */
@Serializable
object AppNavRoutes {

    @Serializable
    data object Home : NavRoute

    @Serializable
    data object Playground : NavRoute
}
