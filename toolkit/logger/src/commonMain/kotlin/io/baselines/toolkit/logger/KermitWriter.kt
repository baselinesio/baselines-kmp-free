package io.baselines.toolkit.logger

import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.platformLogWriter

/**
 * A [LogWriter] implementation that delegates logging to the [Kermit](https://github.com/touchlab/Kermit) library.
 *
 * This writer sets up the Kermit log backend and dynamically provides a tag based on the caller’s class (on Android).
 * On iOS, tagging is disabled (returns null).
 *
 * Initialized with platform-specific behavior via [platformLogWriter] and [tagFactory].
 */
class KermitWriter(
    private val printStacktrace: Boolean,
) : LogWriter {

    init {
        co.touchlab.kermit.Logger.setLogWriters(platformLogWriter(NoTagFormatter))
    }

    override fun i(
        message: () -> String
    ) = co.touchlab.kermit.Logger.i(createTag(), message = message)

    override fun d(
        message: () -> String
    ) = co.touchlab.kermit.Logger.d(createTag(), message = message)

    override fun e(
        throwable: Throwable?,
        message: () -> String
    ) {
        co.touchlab.kermit.Logger.e(
            createTag(),
            message = message,
            throwable = if (printStacktrace) throwable else null
        )
    }

    private fun createTag(): String = tagFactory().createTag() ?: EMPTY_TAG

    private companion object {

        private const val EMPTY_TAG = ""
    }
}
