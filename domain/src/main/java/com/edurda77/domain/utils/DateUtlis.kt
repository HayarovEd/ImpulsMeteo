package com.edurda77.domain.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val localDateTimeFormat = LocalDateTime.Format {
    date(LocalDate.Formats.ISO)
    char(' ')
    hour(); char(':'); minute(); char(':'); second()
}

@OptIn(ExperimentalTime::class)
fun convertToLocalDateTimeOld(stringDate: String): LocalDateTime {
    return try {
        val localDateTime = LocalDateTime.parse(stringDate, localDateTimeFormat)
        localDateTime
    } catch (e:Exception) {
        val timeZone = TimeZone.currentSystemDefault()
        Clock.System.now().toLocalDateTime(timeZone)
    }
}

fun convertToLocalDateTime(stringDate: String): LocalDateTime {
    val instant = Instant.parse(stringDate)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
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
        monthNumber()
    }
    return localDateTime.format(dateFormat)
}

fun formatDateTime(
    localDateTime: LocalDateTime?
): String {
    val dateFormat = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()
        char(' ')
        hour()
        char(':')
        minute()
    }
    return localDateTime?.format(dateFormat)?:""
}
