package com.edurda77.domain.model


data class Auth(
    val accessToken: String,
    val email: String,
    val expiresAt: String,
    val name: String,
)

