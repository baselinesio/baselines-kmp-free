package io.baselines.toolkit.logger

/**
 * iOS-specific implementation of [TagFactory].
 *
 * Since iOS does not support structured log tags in the same way as Android, this returns `null`.
 */
internal class TagFactoryImpl : TagFactory {

    override fun createTag(): String? = null
}

internal actual fun tagFactory(): TagFactory = TagFactoryImpl()
