package io.baselines.ui.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource

@Stable
interface SectionFactory {

    val titleRes: StringResource

    val expandedState: MutableState<Boolean>

    @Composable
    fun Create(expandedState: MutableState<Boolean>)
}
