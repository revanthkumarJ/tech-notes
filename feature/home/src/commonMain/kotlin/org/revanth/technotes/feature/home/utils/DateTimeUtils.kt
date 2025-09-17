package org.revanth.technotes.feature.home.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    fun currentTimeMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    fun Long.toDateString(): String {
        val instant = Instant.fromEpochMilliseconds(this)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dateTime.dayOfMonth}-${dateTime.monthNumber}-${dateTime.year} " +
                "${dateTime.hour}:${dateTime.minute}:${dateTime.second}"
    }
}