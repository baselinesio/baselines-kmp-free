package io.baselines.toolkit.logger

/**
 * Platform-specific interface for generating log tags (usually based on the calling class).
 *
 * On Android, this can extract a tag from the stack trace. On iOS, logging typically doesn’t support tags,
 * so the factory returns `null`.
 */
internal interface TagFactory {

    /**
     * Returns a tag string (e.g., class name) to be used in logs, or `null` if tagging is unsupported.
     */
    fun createTag(): String?
}

/**
 * Platform entry point for resolving the appropriate [TagFactory] implementation.
 */
internal expect fun tagFactory(): TagFactory
