package io.baselines.sample.compose.di

import android.app.Application
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AndroidAppComponent : AppComponent {

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(
            @Includes platformComponent: AndroidPlatformComponent,
            @Provides application: Application,
        ): AndroidAppComponent
    }
}
