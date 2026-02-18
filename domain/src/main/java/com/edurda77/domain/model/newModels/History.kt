package com.edurda77.domain.model.newModels

import kotlinx.datetime.LocalDateTime

data class History(
    val time: LocalDateTime,
    val value: Double
)