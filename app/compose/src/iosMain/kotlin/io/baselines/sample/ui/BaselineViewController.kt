package io.baselines.sample.ui

import androidx.compose.ui.window.ComposeUIViewController
import io.baselines.toolkit.di.UiScope
import me.tatarka.inject.annotations.Inject
import platform.UIKit.UIViewController
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

typealias BaselineViewController = () -> UIViewController

@Inject
@SingleIn(UiScope::class)
fun BaselineViewController(composeApp: ComposeApp) = ComposeUIViewController { composeApp() }
