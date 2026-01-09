package io.baselines.sample.compose.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.createGraphFactory

@DependencyGraph(AppScope::class)
interface IosAppComponent : AppComponent {

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(
            @Includes platformComponent: IosPlatformComponent,
        ): IosAppComponent
    }
}

fun createComponent(platformComponent: IosPlatformComponent): IosAppComponent {
    return createGraphFactory<IosAppComponent.Factory>()
        .create(platformComponent)
}
