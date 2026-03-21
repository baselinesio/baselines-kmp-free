package io.baselines.sample.ui.home

import io.baselines.ui.viewmodel.UiEvent
import io.baselines.ui.viewmodel.UiEventDispatchPolicy

sealed interface HomeUiEvent : UiEvent {

    data object OpenPlayground : HomeUiEvent {
        override val dispatchPolicy = UiEventDispatchPolicy.ThrottleFirst()
    }
}
