package io.baselines.toolkit.time

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.Companion.nowUtc(): Instant = Clock.System.now()

fun Instant.local(): LocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
