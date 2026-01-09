package io.baselines.sample.compose.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.asContribution
import io.baselines.sample.compose.ui.BaselineViewController
import io.baselines.toolkit.di.UiScope

@GraphExtension(UiScope::class)
interface IosUiComponent : UiComponent {

    val baselineViewController: BaselineViewController

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    fun interface Factory {

        fun createUiComponent(): IosUiComponent
    }
}

fun createComponent(appComponent: IosAppComponent): IosUiComponent {
    return appComponent.asContribution<IosUiComponent.Factory>().createUiComponent()
}
