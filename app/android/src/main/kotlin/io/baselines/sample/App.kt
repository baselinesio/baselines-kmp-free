package io.baselines.sample

import io.baselines.sample.compose.di.AndroidPlatformComponent
import io.baselines.sample.compose.ui.BaselineApplication

class App : BaselineApplication() {

    override val platformComponent: AndroidPlatformComponent by lazy { AndroidPlatformComponentImpl(this) }
}
