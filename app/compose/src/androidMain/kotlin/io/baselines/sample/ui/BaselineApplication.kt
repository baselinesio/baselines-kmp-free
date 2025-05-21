package io.baselines.sample.ui

import android.app.Application
import io.baselines.sample.di.AndroidAppComponent
import io.baselines.sample.di.PlatformComponent
import io.baselines.sample.di.create

abstract class BaselineApplication : Application() {

    abstract val platformComponent: PlatformComponent
    val appComponent by lazy {
        AndroidAppComponent::class.create(this, platformComponent)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.compositeInitializer.initialize()
    }
}
