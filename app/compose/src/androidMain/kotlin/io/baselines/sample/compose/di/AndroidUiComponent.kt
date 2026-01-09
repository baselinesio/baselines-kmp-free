package io.baselines.sample.compose.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension
import io.baselines.toolkit.di.UiScope

@GraphExtension(UiScope::class)
interface AndroidUiComponent : UiComponent {

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    fun interface Factory {

        fun createUiComponent(): AndroidUiComponent
    }
}
