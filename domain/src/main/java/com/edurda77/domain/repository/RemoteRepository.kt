package com.edurda77.domain.repository

import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface RemoteRepository {
    suspend fun autorization(email: String, password: String): ResultWork<Auth, DataError>
    suspend fun autorizedUser(token: String): ResultWork<LoggedUser, DataError>
}