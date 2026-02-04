package com.edurda77.domain.repository


interface JwtRepository {
    fun decodeJwt(token: String): Long?
}