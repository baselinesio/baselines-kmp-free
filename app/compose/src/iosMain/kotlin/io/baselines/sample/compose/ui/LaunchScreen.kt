package io.baselines.sample.compose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.baselines.toolkit.di.UiScope
import platform.UIKit.UIStoryboard

@Inject
@SingleIn(UiScope::class)
class LaunchScreen {

    @Composable
    operator fun invoke() {
        UIKitViewController(
            modifier = Modifier.fillMaxSize(),
            factory = {
                val storyboard = UIStoryboard.storyboardWithName(name = "LaunchScreen", bundle = null)
                storyboard.instantiateInitialViewController()!!
            }
        )
    }
}
