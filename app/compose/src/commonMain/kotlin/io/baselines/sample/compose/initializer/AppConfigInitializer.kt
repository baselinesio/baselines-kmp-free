package io.baselines.sample.compose.initializer

import dev.zacsweers.metro.Inject
import io.baselines.toolkit.config.AppConfig
import io.baselines.toolkit.config.AppConfigManager
import io.baselines.toolkit.initializer.Initializer

@Inject
class AppConfigInitializer(
    private val appInfo: AppConfig.AppInfo,
    private val appConfigManager: AppConfigManager,
) : Initializer {

    override suspend fun init() {
        appConfigManager.update { it.copy(appInfo = appInfo) }
    }
}
