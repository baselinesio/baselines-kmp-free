package io.baselines.ui.playground.screen

import io.baselines.ui.viewmodel.UiEvent

sealed interface PlaygroundUiEvent : UiEvent {

    data class Search(val input: String) : PlaygroundUiEvent
}
