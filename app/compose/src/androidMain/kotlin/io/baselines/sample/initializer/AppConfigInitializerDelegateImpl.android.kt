package io.baselines.sample.initializer

import android.app.Application
import io.baselines.sample.BuildConfig
import io.baselines.toolkit.config.AppConfig
import io.baselines.toolkit.config.AppConfigManager
import me.tatarka.inject.annotations.Inject

@Inject
class AppConfigInitializerDelegateImpl(
    private val appConfigManager: AppConfigManager,
    private val application: Application,
) : AppConfigInitializerDelegate {

    override suspend fun init() {
        val info = application.packageManager
            .getPackageInfo(application.packageName, 0)
        appConfigManager.update {
            it.copy(
                debug = BuildConfig.DEBUG,
                version = info.versionName.orEmpty(),
                platform = AppConfig.Platform.ANDROID,
            )
        }
    }
}
