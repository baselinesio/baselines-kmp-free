package io.baselines.sample.compose.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Multibinds
import io.baselines.sample.compose.initializer.AppConfigInitializer
import io.baselines.sample.compose.initializer.LoggerInitializer
import io.baselines.sample.compose.initializer.StartRouteInitializer
import io.baselines.toolkit.initializer.AsyncInitializer
import io.baselines.toolkit.initializer.Initializer

@ContributesTo(AppScope::class)
interface InitializersProvider {

    @Multibinds(allowEmpty = true)
    val coreInitializers: Map<Int, Initializer>

    @Multibinds(allowEmpty = true)
    val asyncInitializers: Map<Int, AsyncInitializer>

    @Binds
    @IntoMap
    @IntKey(0)
    val AppConfigInitializer.bind: Initializer

    @Binds
    @IntoMap
    @IntKey(1)
    val LoggerInitializer.bind: Initializer

    @Binds
    @IntoMap
    @IntKey(2)
    val StartRouteInitializer.bind: AsyncInitializer
}
