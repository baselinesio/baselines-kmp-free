package io.baselines.sample.compose.di

import io.baselines.sample.compose.ui.BaselineViewController
import io.baselines.toolkit.di.UiScope
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesSubcomponent(UiScope::class)
@SingleIn(UiScope::class)
interface IosUiComponent : UiComponent {

    val baselineViewController: BaselineViewController

    @ContributesSubcomponent.Factory(AppScope::class)
    interface Factory {
        fun createUiComponent(): IosUiComponent
    }
}
