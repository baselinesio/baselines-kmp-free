package io.baselines.sample

import io.baselines.sample.di.PlatformComponent
import io.baselines.sample.ui.BaselineApplication

class App : BaselineApplication() {

    override val platformComponent: PlatformComponent by lazy { PlatformComponentImpl() }
}
