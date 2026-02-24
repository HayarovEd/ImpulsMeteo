package com.edurda77.domain.repository

import com.edurda77.domain.model.Token
import com.edurda77.domain.model.AuthUser
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface RemoteRepository {
    suspend fun authorization(email: String, password: String): ResultWork<Token, DataError>
    suspend fun refresh(refreshToken: String): ResultWork<Token, DataError>
    suspend fun logout(refreshToken: String): ResultWork<Unit, DataError>
    suspend fun loadAuthUserData(accessToken: String): ResultWork<AuthUser, DataError>
}