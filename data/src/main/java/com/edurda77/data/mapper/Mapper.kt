package com.edurda77.data.mapper

import com.edurda77.data.remote.auth.AuthDto
import com.edurda77.data.remote.auth_user.AuthUserDto
import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.utils.DEVICES_LIST
import com.edurda77.domain.utils.DIRECTORY_LIST
import com.edurda77.domain.utils.NEGATIVE_USER_ID
import com.edurda77.domain.utils.USERS_LIST
import com.edurda77.domain.utils.convertToLocalDateTime


fun AuthDto.convertToAuth(): Auth {
    return Auth(
        accessToken = this.accessToken,
        expiresAt = convertToLocalDateTime(this.expiresAt),
        id = if (this.permissions.isNotEmpty()) {
            val containsRequired =
                this.permissions.any { it.id == USERS_LIST || it.id == DEVICES_LIST || it.id == DIRECTORY_LIST }
            if (containsRequired)
                this.permissions.first().id else NEGATIVE_USER_ID
        } else NEGATIVE_USER_ID
    )
}

fun AuthUserDto.convertToLoggedUser(): LoggedUser {
    return LoggedUser(
        id = this.permissions.first().pivot.userId,
        name = this.name,
        email = this.email,
        permissions = this.permissions.map { it.id }
    )
}

