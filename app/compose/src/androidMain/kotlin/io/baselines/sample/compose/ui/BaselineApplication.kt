package io.baselines.sample.compose.ui

import android.app.Application
import io.baselines.sample.compose.di.AndroidAppComponent
import io.baselines.sample.compose.di.AndroidPlatformComponent
import io.baselines.sample.compose.di.create

abstract class BaselineApplication : Application() {

    abstract val platformComponent: AndroidPlatformComponent
    val appComponent by lazy {
        AndroidAppComponent::class.create(this, platformComponent)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.compositeInitializer.initialize()
    }
}
