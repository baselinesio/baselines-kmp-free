package io.baselines.sample.domain

import dev.zacsweers.metro.Inject
import io.baselines.sample.domain.api.NavRoute
import io.baselines.sample.domain.api.Worker

/**
 * This is the perfect place to put logic that decides what screen should be shown at the first place.
 * E.g. based on auth stated you can decide whether to show Login or Home screen.
 */
@Inject
class GetStartRoute : Worker<Unit, NavRoute>() {

    override suspend fun doWork(params: Unit): NavRoute = NavRoute.Default
}
