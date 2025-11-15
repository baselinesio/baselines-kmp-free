package io.baselines.sample.compose.initializer

import io.baselines.toolkit.config.AppConfig
import io.baselines.toolkit.config.AppConfigManager
import io.baselines.toolkit.initializer.Initializer
import me.tatarka.inject.annotations.Inject

@Inject
class AppConfigInitializer(
    private val appInfo: AppConfig.AppInfo,
    private val appConfigManager: AppConfigManager,
) : Initializer {

    override suspend fun init() {
        appConfigManager.update { it.copy(info = appInfo) }
    }
}
