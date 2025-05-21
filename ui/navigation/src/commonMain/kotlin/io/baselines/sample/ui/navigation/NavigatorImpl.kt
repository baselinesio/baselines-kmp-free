package io.baselines.sample.ui.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

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
