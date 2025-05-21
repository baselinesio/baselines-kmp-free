package io.baselines.toolkit.logger

import java.util.regex.Pattern

/**
 * Android-specific implementation of [TagFactory] that infers the calling class from the stack trace.
 *
 * This enables class-level tagging similar to traditional Android logging (Logcat).
 */
internal class TagFactoryImpl : TagFactory {

    override fun createTag(): String? {
        val classNames = Throwable().stackTrace.mapNotNull(this::parseClassName)
        return classNames.firstOrNull { it !in LOGGER_CLASSES }
    }

    private fun parseClassName(element: StackTraceElement): String {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        return tag
    }

    private companion object {

        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
        private val LOGGER_CLASSES = listOf(
            TagFactoryImpl::class.simpleName,
            KermitWriter::class.simpleName,
            Logger::class.simpleName,
        )
    }
}

internal actual fun tagFactory(): TagFactory = TagFactoryImpl()
