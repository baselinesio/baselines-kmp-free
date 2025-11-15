package io.baselines.sample.compose.di

import io.baselines.toolkit.coroutines.AppDispatchers
import io.baselines.toolkit.initializer.CompositeInitializer
import me.tatarka.inject.annotations.Provides

interface AppComponent {

    val platformComponent: PlatformComponent
    val compositeInitializer: CompositeInitializer

    @Provides
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers()
}
