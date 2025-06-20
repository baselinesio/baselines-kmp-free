package io.baselines.ui.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
interface SectionFactory {

    val name: String

    @Composable
    fun Create()
}
