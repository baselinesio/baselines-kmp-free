package io.baselines.ui.viewmodel

import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class UiEventDispatcher<E : UiEvent>(
    private val handler: (E) -> Unit,
    private val scope: CoroutineScope,
    private val timeSource: TimeSource.WithComparableMarks = TimeSource.Monotonic,
) {

    private val throttleEntries = mutableMapOf<Any, ThrottleEntry>()
    private val debounceJobs = mutableMapOf<Any, Job>()

    fun dispatch(event: E) {
        when (val policy = event.dispatchPolicy) {
            UiEventDispatchPolicy.Immediate -> dispatchImmediate(event)
            is UiEventDispatchPolicy.ThrottleFirst -> dispatchThrottleFirst(event, policy)
            is UiEventDispatchPolicy.DebounceLatest -> dispatchDebounceLatest(event, policy)
        }
    }

    private fun dispatchImmediate(event: E) {
        cancelPendingDebounceJob(resolveKey(event))
        handler(event)
    }

    private fun dispatchThrottleFirst(
        event: E,
        policy: UiEventDispatchPolicy.ThrottleFirst,
    ) {
        val key = resolveKey(event, policy.key)
        cancelPendingDebounceJob(key)
        pruneExpiredThrottleEntries()

        val existingEntry = throttleEntries[key]
        if (existingEntry != null && existingEntry.mark.elapsedNow() < existingEntry.window) {
            return
        }

        throttleEntries[key] = ThrottleEntry(
            mark = timeSource.markNow(),
            window = policy.window,
        )
        handler(event)
    }

    private fun dispatchDebounceLatest(
        event: E,
        policy: UiEventDispatchPolicy.DebounceLatest,
    ) {
        val key = resolveKey(event, policy.key)
        debounceJobs.remove(key)?.cancel()

        val job = scope.launch {
            delay(policy.window)
            handler(event)
        }
        debounceJobs[key] = job
        job.invokeOnCompletion {
            if (debounceJobs[key] === job) {
                debounceJobs.remove(key)
            }
        }
    }

    private fun pruneExpiredThrottleEntries() {
        val iterator = throttleEntries.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.mark.elapsedNow() >= entry.value.window) {
                iterator.remove()
            }
        }
    }

    private fun resolveKey(
        event: E,
        customKey: Any? = null,
    ): Any = customKey ?: event::class

    private fun cancelPendingDebounceJob(key: Any) {
        debounceJobs.remove(key)?.cancel()
    }

    private data class ThrottleEntry(
        val mark: TimeMark,
        val window: Duration,
    )
}
