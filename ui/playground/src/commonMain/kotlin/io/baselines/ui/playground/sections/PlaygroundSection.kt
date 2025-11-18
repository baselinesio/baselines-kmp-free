package io.baselines.ui.playground.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface PlaygroundSection {

    val name: String

    val uiFactory: UiFactory

    @Immutable
    data class UiFactory(val composable: @Composable () -> Unit)
}
