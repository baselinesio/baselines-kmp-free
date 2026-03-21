package io.baselines.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

/**
 * Creates a stable event sink backed by a reusable [UiEventDispatcher].
 *
 * Store the returned lambda in a ViewModel property and pass it through [UiState.eventSink]. This keeps
 * dispatch state, including debounce windows, stable across recompositions and repeated [Mvvm.state] calls.
 */
fun <E : UiEvent> ViewModel.createEventSink(
    handler: (E) -> Unit,
): (E) -> Unit {
    val dispatcher = UiEventDispatcher(
        handler = handler,
        scope = viewModelScope,
    )
    return dispatcher::dispatch
}
