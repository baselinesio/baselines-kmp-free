package io.baselines.sample.compose.ui

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import io.baselines.sample.compose.di.AndroidAppComponent
import io.baselines.sample.compose.di.AndroidPlatformComponent

abstract class BaselineApplication : Application() {

    abstract val platformComponent: AndroidPlatformComponent
    val appComponent by lazy {
        createGraphFactory<AndroidAppComponent.Factory>()
            .create(platformComponent, this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.compositeInitializer.initialize()
    }
}
