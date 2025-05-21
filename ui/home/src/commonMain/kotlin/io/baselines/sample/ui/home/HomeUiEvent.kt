package io.baselines.sample.ui.home

import io.baselines.ui.viewmodel.UiEvent

sealed interface HomeUiEvent : UiEvent {

    data object OpenPlayground : HomeUiEvent
}
