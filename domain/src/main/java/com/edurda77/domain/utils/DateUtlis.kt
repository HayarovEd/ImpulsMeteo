package com.edurda77.domain.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

private val localDateTimeFormat = LocalDateTime.Format {
    date(LocalDate.Formats.ISO)
    char(' ')
    hour(); char(':'); minute(); char(':'); second()
}

fun convertToLocalDateTime(stringDate: String): LocalDateTime {
    return try {
        val localDateTime = LocalDateTime.parse(stringDate, localDateTimeFormat)
        localDateTime
    } catch (e:Exception) {
        val timeZone = TimeZone.currentSystemDefault()
        Clock.System.now().toLocalDateTime(timeZone)
    }
}

fun convertToStringDateTime(localDateTime: LocalDateTime): String {
    return localDateTime.format(
        localDateTimeFormat
    )
}

fun formatDateTimeChart(
    localDateTime: LocalDateTime
): String {
    val dateFormat = LocalDateTime.Format {
        hour()
        char(':')
        minute()
        char('\n')
        monthNumber()
        char('/')
        dayOfMonth()
    }
    return localDateTime.format(dateFormat)
}
