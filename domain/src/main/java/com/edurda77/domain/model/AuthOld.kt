package com.edurda77.domain.model

import kotlinx.datetime.LocalDateTime


data class AuthOld(
    val accessToken: String,
    val expiresAt: LocalDateTime,
    val id: Int,
)

