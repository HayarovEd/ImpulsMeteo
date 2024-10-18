package com.edurda77.domain.model

import kotlinx.datetime.LocalDateTime


data class Auth(
    val accessToken: String,
    val expiresAt: LocalDateTime,
    val id: Int,
)

