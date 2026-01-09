package io.baselines.toolkit.config

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class AppConfigManagerImpl : AppConfigManager {

    private val appConfigStateFlow = MutableStateFlow(AppConfig())

    override val appConfig: StateFlow<AppConfig> = appConfigStateFlow.asStateFlow()

    override fun update(updater: (AppConfig) -> AppConfig) = appConfigStateFlow.update { updater.invoke(it) }
}
