package io.baselines.sample.di

import io.baselines.sample.initializer.AppConfigInitializer
import io.baselines.sample.initializer.LoggerInitializer
import io.baselines.sample.initializer.StartRouteInitializer
import io.baselines.toolkit.initializer.AsyncInitializer
import io.baselines.toolkit.initializer.Initializer
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

/*
* !!! IMPORTANT: Order of the initializers' bindings matter. First defined initializer is the first one to execute.
*/
@ContributesTo(AppScope::class)
interface InitializersComponent {

    val AppConfigInitializer.bind: Initializer
        @Provides @IntoSet get() = this

    // Logger depends on AppConfig, so should be initialized after it.
    val LoggerInitializer.bind: Initializer
        @Provides @IntoSet get() = this

    val StartRouteInitializer.bind: AsyncInitializer
        @Provides @IntoSet get() = this
}
