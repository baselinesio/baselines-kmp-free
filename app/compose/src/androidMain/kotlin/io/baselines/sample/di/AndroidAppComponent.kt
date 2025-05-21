package io.baselines.sample.di

import android.app.Application
import io.baselines.sample.initializer.AppConfigInitializerDelegate
import io.baselines.sample.initializer.AppConfigInitializerDelegateImpl
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class AndroidAppComponent(
    @get:Provides val application: Application,
    @Component override val platformComponent: PlatformComponent
) : AppComponent {

    abstract val uiComponentFactory: AndroidUiComponent.Factory

    val AppConfigInitializerDelegateImpl.bind: AppConfigInitializerDelegate
        @Provides get() = this
}
