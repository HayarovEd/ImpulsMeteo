package com.edurda77.data.mapper

import com.edurda77.data.remote.auth.AuthDto
import com.edurda77.domain.model.Auth
import com.edurda77.domain.utils.convertToLocalDateTime


fun AuthDto.convertToAuth(): Auth {
    return Auth(
        accessToken = this.accessToken,
        expiresAt = convertToLocalDateTime(this.expiresAt),
        id = if (this.permissions.isNotEmpty()) this.permissions.first().id else -1
    )
}