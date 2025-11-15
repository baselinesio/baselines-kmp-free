package io.baselines.sample.compose.ui

import androidx.compose.ui.window.ComposeUIViewController
import io.baselines.toolkit.di.UiScope
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(UiScope::class)
class BaselineViewController(private val composeApp: ComposeApp) {

    operator fun invoke() = ComposeUIViewController { composeApp() }
}
