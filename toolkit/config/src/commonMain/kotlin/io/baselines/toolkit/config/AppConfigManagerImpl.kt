package io.baselines.toolkit.config

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AppConfigManagerImpl : AppConfigManager {

    private val appConfigStateFlow = MutableStateFlow(AppConfig())

    override val appConfig: StateFlow<AppConfig> = appConfigStateFlow.asStateFlow()

    override fun update(updater: (AppConfig) -> AppConfig) = appConfigStateFlow.update { updater.invoke(it) }
}
