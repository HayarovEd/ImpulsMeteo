package com.edurda77.domain.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

private val localDateTimeFormat = LocalDateTime.Format {
    date(LocalDate.Formats.ISO)
    char(' ')
    hour(); char(':'); minute(); char(':'); second()
}

fun convertToLocalDateTime(stringDate: String): LocalDateTime {
    val localDateTime = LocalDateTime.parse(stringDate, localDateTimeFormat)
    return localDateTime
}

fun convertToStringDateTime(localDateTime: LocalDateTime): String {
    return localDateTime.format(
        localDateTimeFormat
    )
}
