package io.baselines.sample.initializer

import io.baselines.toolkit.initializer.Initializer
import me.tatarka.inject.annotations.Inject

@Inject
class AppConfigInitializer(
    private val appConfigInitializerDelegate: AppConfigInitializerDelegate,
) : Initializer {

    override suspend fun init() = appConfigInitializerDelegate.init()
}
