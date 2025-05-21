package io.baselines.sample

import io.baselines.sample.di.PlatformComponent

/**
 * Android-specific implementation of [PlatformComponent].
 *
 * This class provides actual Android platform dependencies required by shared app code.
 * It is typically instantiated in the Android application module and passed into the KMP bootstrap flow.
 *
 * @see [App] for initialization and usage context
 */
class PlatformComponentImpl : PlatformComponent
