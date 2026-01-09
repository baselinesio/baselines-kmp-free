package io.baselines.sample.compose.initializer

import dev.zacsweers.metro.Inject
import io.baselines.sample.domain.GetStartRoute
import io.baselines.sample.domain.api.NavRoute
import io.baselines.toolkit.initializer.AsyncInitializer

@Inject
class StartRouteInitializer(
    private val getStartRoute: GetStartRoute,
) : AsyncInitializer {

    override suspend fun init() {
        getStartRoute(Unit)
            .onSuccess { NavRoute.Default = it }
    }
}
