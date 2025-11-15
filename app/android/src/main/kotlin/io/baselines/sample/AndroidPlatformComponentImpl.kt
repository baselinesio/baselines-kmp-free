package io.baselines.sample

import android.app.Application
import io.baselines.sample.compose.di.AndroidPlatformComponent
import io.baselines.sample.compose.di.PlatformComponent
import io.baselines.toolkit.config.AppConfig

/**
 * Android-specific implementation of [PlatformComponent].
 *
 * This class provides actual Android platform dependencies required by shared app code.
 * It is typically instantiated in the Android application module and passed into the KMP bootstrap flow.
 *
 * @see [App] for initialization and usage context
 */
class AndroidPlatformComponentImpl(
    application: Application
) : AndroidPlatformComponent {

    private val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)

    override val appInfo = AppConfig.AppInfo(
        debug = BuildConfig.DEBUG,
        platform = AppConfig.Platform.ANDROID,
        version = packageInfo.versionName.orEmpty(),
    )
}
