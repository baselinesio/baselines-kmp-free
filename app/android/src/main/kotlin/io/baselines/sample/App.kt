package io.baselines.sample

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import io.baselines.sample.compose.di.AndroidAppComponent
import io.baselines.sample.compose.di.AndroidPlatformComponent

class App : Application() {

    private val platformComponent: AndroidPlatformComponent by lazy { AndroidPlatformComponentImpl(this) }

    val appComponent by lazy {
        createGraphFactory<AndroidAppComponent.Factory>()
            .create(platformComponent, this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.compositeInitializer.initialize()
    }
}
