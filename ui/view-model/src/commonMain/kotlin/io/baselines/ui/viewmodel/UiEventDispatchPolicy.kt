package io.baselines.ui.viewmodel

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Describes how a [UiEvent] should be forwarded through the shared ViewModel event pipeline.
 *
 * This policy is evaluated by the shared `createEventSink(...)` dispatcher before the event reaches the
 * ViewModel handler. Use [Immediate] for events that should always be delivered, [ThrottleFirst] for
 * click-like interactions that should drop repeated emissions inside a short time window, and
 * [DebounceLatest] for latest-value interactions that should emit only after input settles.
 */
sealed interface UiEventDispatchPolicy {

    /**
     * Delivers every event immediately without any filtering or delay.
     *
     * This is the default policy for [UiEvent] and is appropriate for continuous or stateful inputs
     * such as text changes, sliders, toggles, or any event where repeated emissions are expected.
     */
    data object Immediate : UiEventDispatchPolicy

    /**
     * Delivers the first event for a dispatch key immediately, then drops subsequent events for the same
     * key until [window] has elapsed.
     *
     * This is appropriate for one-shot tap actions such as navigation, submit, next, or close buttons.
     *
     * @property window The minimum time that must pass before the same dispatch key may be accepted again.
     * Defaults to `500.milliseconds`.
     * @property key Optional custom dispatch key. When `null`, the event class is used so all instances of
     * the same event type share one throttle lane. Provide a custom key when one event class should keep
     * multiple independent lanes.
     */
    data class ThrottleFirst(
        val window: Duration = 500.milliseconds,
        val key: Any? = null,
    ) : UiEventDispatchPolicy

    /**
     * Delays delivery until [window] passes without a newer event arriving for the same dispatch key.
     *
     * Only the latest event for a key is delivered. This is appropriate for search, filtering, and other
     * latest-value interactions where intermediate emissions should be superseded by newer input.
     *
     * @property window The idle period that must pass before the latest event is delivered.
     * Defaults to `500.milliseconds`.
     * @property key Optional custom dispatch key. When `null`, the event class is used so all instances of
     * the same event type share one debounce lane. Provide a custom key when one event class should keep
     * multiple independent lanes.
     */
    data class DebounceLatest(
        val window: Duration = 500.milliseconds,
        val key: Any? = null,
    ) : UiEventDispatchPolicy
}
