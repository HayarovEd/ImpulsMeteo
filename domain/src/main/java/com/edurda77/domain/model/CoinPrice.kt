package com.edurda77.domain.model

import kotlinx.datetime.LocalDateTime

data class CoinPrice(
    val priceUsd: Double,
    val dateTime: LocalDateTime
)
