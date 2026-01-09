package io.baselines.sample.ui.navigation

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.baselines.sample.domain.api.NavRoute
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class NavigatorImpl : Navigator {

    private val eventStateFlow = MutableSharedFlow<NavEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val events: Flow<NavEvent> = eventStateFlow.asSharedFlow()

    override fun navigate(route: NavRoute, navOptionsBuilder: RouteNavOptionsBuilder?) {
        eventStateFlow.tryEmit(NavEvent.Navigate(route, navOptionsBuilder))
    }

    override fun navigateBack(route: NavRoute?, inclusive: Boolean) {
        eventStateFlow.tryEmit(NavEvent.Back(route, inclusive))
    }

    override fun navigateUp() {
        eventStateFlow.tryEmit(NavEvent.Up())
    }
}
