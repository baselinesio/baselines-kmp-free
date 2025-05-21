@file:OptIn(ExperimentalNativeApi::class)

package io.baselines.sample.initializer

import io.baselines.toolkit.config.AppConfig
import io.baselines.toolkit.config.AppConfigManager
import kotlin.experimental.ExperimentalNativeApi
import me.tatarka.inject.annotations.Inject
import platform.Foundation.NSBundle

@Inject
class AppConfigInitializerDelegateImpl(
    private val appConfigManager: AppConfigManager,
) : AppConfigInitializerDelegate {

    override suspend fun init() {
        val info = NSBundle.mainBundle.infoDictionary
        val version = info?.get("CFBundleShortVersionString") as? String
        appConfigManager.update {
            it.copy(
                debug = Platform.isDebugBinary,
                version = version.orEmpty(),
                platform = AppConfig.Platform.IOS,
            )
        }
    }
}
