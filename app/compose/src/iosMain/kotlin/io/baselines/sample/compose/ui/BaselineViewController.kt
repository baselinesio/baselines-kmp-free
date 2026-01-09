package io.baselines.sample.compose.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.baselines.toolkit.di.UiScope
import io.baselines.toolkit.initializer.CompositeInitializer
import platform.UIKit.UIViewController

@Inject
@SingleIn(UiScope::class)
class BaselineViewController(
    private val composeApp: ComposeApp,
    private val compositeInitializer: CompositeInitializer,
    private val launchScreen: LaunchScreen,
) {

    operator fun invoke(): UIViewController = ComposeUIViewController {
        val initializationResult by compositeInitializer.resultFlow.collectAsState(null)
        AnimatedContent(
            targetState = initializationResult == null,
        ) { loading ->
            if (loading) {
                launchScreen()
            } else {
                composeApp()
            }
        }
    }
}
