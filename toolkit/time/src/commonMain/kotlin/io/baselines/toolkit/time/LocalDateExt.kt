package io.baselines.toolkit.time

import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime

fun LocalDate.atStartOfDay(): LocalDateTime = atTime(LocalTime(0, 0))

fun LocalDate.Companion.now(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return Clock.System.now().toLocalDateTime(zone).date
}
