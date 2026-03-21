package io.baselines.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TestTimeSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class UiEventDispatcherTest {

    @Test
    fun dispatch_deliversEveryEvent_whenPolicyIsImmediate() = runTest {
        val acceptedEvents = mutableListOf<TestUiEvent>()
        val dispatcher = UiEventDispatcher<TestUiEvent>(
            handler = { acceptedEvents += it },
            scope = backgroundScope,
            timeSource = TestTimeSource(),
        )

        dispatcher.dispatch(TestUiEvent.ImmediateTap)
        dispatcher.dispatch(TestUiEvent.ImmediateTap)

        assertEquals(
            expected = listOf<TestUiEvent>(
                TestUiEvent.ImmediateTap,
                TestUiEvent.ImmediateTap,
            ),
            actual = acceptedEvents,
        )
    }

    @Test
    fun dispatch_dropsRepeatedEvents_whenThrottleFirstWindowIsActive() = runTest {
        val acceptedEvents = mutableListOf<TestUiEvent>()
        val dispatcher = UiEventDispatcher<TestUiEvent>(
            handler = { acceptedEvents += it },
            scope = backgroundScope,
            timeSource = TestTimeSource(),
        )

        dispatcher.dispatch(TestUiEvent.ThrottledTap)
        dispatcher.dispatch(TestUiEvent.ThrottledTap)

        assertEquals(
            expected = listOf<TestUiEvent>(TestUiEvent.ThrottledTap),
            actual = acceptedEvents,
        )
    }

    @Test
    fun dispatch_deliversEventAgain_whenThrottleFirstWindowHasExpired() = runTest {
        val timeSource = TestTimeSource()
        val acceptedEvents = mutableListOf<TestUiEvent>()
        val dispatcher = UiEventDispatcher<TestUiEvent>(
            handler = { acceptedEvents += it },
            scope = backgroundScope,
            timeSource = timeSource,
        )

        dispatcher.dispatch(TestUiEvent.ThrottledTap)
        timeSource += 501.milliseconds
        dispatcher.dispatch(TestUiEvent.ThrottledTap)

        assertEquals(
            expected = listOf<TestUiEvent>(
                TestUiEvent.ThrottledTap,
                TestUiEvent.ThrottledTap,
            ),
            actual = acceptedEvents,
        )
    }

    @Test
    fun dispatch_deliversBothEvents_whenThrottleFirstKeysAreDifferent() = runTest {
        val acceptedEvents = mutableListOf<TestUiEvent>()
        val dispatcher = UiEventDispatcher<TestUiEvent>(
            handler = { acceptedEvents += it },
            scope = backgroundScope,
            timeSource = TestTimeSource(),
        )

        dispatcher.dispatch(TestUiEvent.PartitionedTap("primary"))
        dispatcher.dispatch(TestUiEvent.PartitionedTap("secondary"))

        assertEquals(
            expected = listOf<TestUiEvent>(
                TestUiEvent.PartitionedTap("primary"),
                TestUiEvent.PartitionedTap("secondary"),
            ),
            actual = acceptedEvents,
        )
    }

    @Test
    fun dispatch_deliversOnlyLatestEvent_whenDebounceLatestWindowSettles() = runTest {
        val acceptedEvents = mutableListOf<TestUiEvent>()
        val dispatcher = UiEventDispatcher<TestUiEvent>(
            handler = { acceptedEvents += it },
            scope = backgroundScope,
            timeSource = TestTimeSource(),
        )

        dispatcher.dispatch(TestUiEvent.Search("vision"))
        runCurrent()
        advanceTimeBy(250)
        dispatcher.dispatch(TestUiEvent.Search("health"))
        runCurrent()
        advanceTimeBy(499)

        assertEquals(
            expected = emptyList<TestUiEvent>(),
            actual = acceptedEvents,
        )

        advanceTimeBy(1)
        runCurrent()

        assertEquals(
            expected = listOf<TestUiEvent>(TestUiEvent.Search("health")),
            actual = acceptedEvents,
        )
    }

    @Test
    fun dispatch_deliversIndependentEvents_whenDebounceLatestKeysAreDifferent() = runTest {
        val acceptedEvents = mutableListOf<TestUiEvent>()
        val dispatcher = UiEventDispatcher<TestUiEvent>(
            handler = { acceptedEvents += it },
            scope = backgroundScope,
            timeSource = TestTimeSource(),
        )

        dispatcher.dispatch(TestUiEvent.PartitionedSearch(group = "one", query = "vision"))
        dispatcher.dispatch(TestUiEvent.PartitionedSearch(group = "two", query = "health"))
        runCurrent()
        advanceTimeBy(500)
        runCurrent()

        assertEquals(
            expected = listOf<TestUiEvent>(
                TestUiEvent.PartitionedSearch(group = "one", query = "vision"),
                TestUiEvent.PartitionedSearch(group = "two", query = "health"),
            ),
            actual = acceptedEvents,
        )
    }

    @Test
    fun dispatch_cancelsPendingDebounceLatest_whenImmediateEventOfSameTypeArrives() = runTest {
        val acceptedEvents = mutableListOf<TestUiEvent>()
        val dispatcher = UiEventDispatcher<TestUiEvent>(
            handler = { acceptedEvents += it },
            scope = backgroundScope,
            timeSource = TestTimeSource(),
        )

        dispatcher.dispatch(TestUiEvent.ClearableSearch("vision"))
        runCurrent()
        advanceTimeBy(250)
        dispatcher.dispatch(TestUiEvent.ClearableSearch(""))
        advanceTimeBy(500)

        assertEquals(
            expected = listOf<TestUiEvent>(TestUiEvent.ClearableSearch("")),
            actual = acceptedEvents,
        )
    }

    @Test
    fun stateSink_preservesThrottleState_whenViewModelReusesStableEventSink() {
        val viewModel = TestViewModel()
        val firstStateSink = viewModel.stateSink()
        val secondStateSink = viewModel.stateSink()

        firstStateSink(TestUiEvent.ThrottledTap)
        secondStateSink(TestUiEvent.ThrottledTap)

        assertEquals(
            expected = listOf<TestUiEvent>(TestUiEvent.ThrottledTap),
            actual = viewModel.acceptedEvents,
        )
    }

    private sealed interface TestUiEvent : UiEvent {
        data object ImmediateTap : TestUiEvent

        data object ThrottledTap : TestUiEvent {
            override val dispatchPolicy: UiEventDispatchPolicy = UiEventDispatchPolicy.ThrottleFirst()
        }

        data class PartitionedTap(
            val lane: String,
        ) : TestUiEvent {
            override val dispatchPolicy: UiEventDispatchPolicy =
                UiEventDispatchPolicy.ThrottleFirst(key = lane)
        }

        data class Search(
            val query: String,
        ) : TestUiEvent {
            override val dispatchPolicy: UiEventDispatchPolicy = UiEventDispatchPolicy.DebounceLatest()
        }

        data class PartitionedSearch(
            val group: String,
            val query: String,
        ) : TestUiEvent {
            override val dispatchPolicy: UiEventDispatchPolicy =
                UiEventDispatchPolicy.DebounceLatest(key = group)
        }

        data class ClearableSearch(
            val query: String,
        ) : TestUiEvent {
            override val dispatchPolicy: UiEventDispatchPolicy =
                if (query.isEmpty()) {
                    UiEventDispatchPolicy.Immediate
                } else {
                    UiEventDispatchPolicy.DebounceLatest()
                }
        }
    }

    private class TestViewModel : ViewModel() {
        val acceptedEvents = mutableListOf<TestUiEvent>()
        private val stableEventSink = createEventSink<TestUiEvent> { acceptedEvents += it }

        fun stateSink(): (TestUiEvent) -> Unit = stableEventSink
    }
}
