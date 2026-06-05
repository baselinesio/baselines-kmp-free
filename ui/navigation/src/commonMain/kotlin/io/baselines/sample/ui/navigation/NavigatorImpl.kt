package io.baselines.sample.ui.navigation

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import io.baselines.sample.domain.api.NavRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class NavigatorImpl : Navigator {

    private val eventsChannel = Channel<NavEvent>(capacity = Channel.UNLIMITED)

    override val events: Flow<NavEvent> = eventsChannel.receiveAsFlow()

    override fun navigate(route: NavRoute, navOptionsBuilder: RouteNavOptionsBuilder?) {
        emit(NavEvent.Navigate(route, navOptionsBuilder))
    }

    override fun navigateBack(route: NavRoute?, inclusive: Boolean) {
        emit(NavEvent.Back(route, inclusive))
    }

    override fun navigateUp() {
        emit(NavEvent.Up())
    }

    private fun emit(event: NavEvent) {
        eventsChannel.trySend(event)
    }
}
