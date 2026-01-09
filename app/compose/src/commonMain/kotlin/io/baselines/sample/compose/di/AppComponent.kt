package io.baselines.sample.compose.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import io.baselines.toolkit.coroutines.AppDispatchers
import io.baselines.toolkit.initializer.CompositeInitializer

interface AppComponent : ViewModelGraph {

    val compositeInitializer: CompositeInitializer

    @Provides
    @SingleIn(AppScope::class)
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers()
}
