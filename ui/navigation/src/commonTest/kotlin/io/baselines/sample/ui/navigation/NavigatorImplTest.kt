package io.baselines.sample.ui.navigation

import io.baselines.sample.domain.api.AppNavRoutes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class NavigatorImplTest {

    @Test
    fun events_deliversEveryNavigationEventInOrder_whenEmittedInBurst() = runTest {
        val navigator = NavigatorImpl()
        val events = mutableListOf<NavEvent>()
        val collectJob = launch {
            navigator.events
                .take(EXPECTED_EVENTS_COUNT)
                .toList(events)
        }

        navigator.navigate(AppNavRoutes.Home)
        navigator.navigate(AppNavRoutes.Playground)
        navigator.navigateBack()
        navigator.navigateUp()
        runCurrent()
        collectJob.join()

        assertEquals(EXPECTED_EVENTS_COUNT, events.size)
        assertEquals(AppNavRoutes.Home, assertIs<NavEvent.Navigate>(events[0]).route)
        assertEquals(AppNavRoutes.Playground, assertIs<NavEvent.Navigate>(events[1]).route)
        assertIs<NavEvent.Back>(events[2])
        assertIs<NavEvent.Up>(events[3])
    }

    private companion object {
        const val EXPECTED_EVENTS_COUNT = 4
    }
}
