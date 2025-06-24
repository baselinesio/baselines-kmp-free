package io.baselines.sample.initializer

import io.baselines.toolkit.config.AppConfig.Platform
import io.baselines.toolkit.config.AppConfigManager
import io.baselines.toolkit.initializer.Initializer
import io.baselines.toolkit.logger.KermitWriter
import io.baselines.toolkit.logger.Logger
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject

@Inject
class LoggerInitializer(
    private val appConfigManager: AppConfigManager,
) : Initializer {

    override suspend fun init() {
        val config = appConfigManager.appConfig.first()
        if (config.debug) {
            Logger.writer = KermitWriter(
                printStacktrace = config.platform != Platform.IOS
            )
        }
    }
}
