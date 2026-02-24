package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toAuthUser
import com.edurda77.domain.model.Token
import com.edurda77.domain.model.AuthUser
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EMAIL
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.PASSWORD
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteRepositoryImpl(
    private val httpClient: HttpClient
) : RemoteRepository {
    override suspend fun authorization(
        email: String,
        password: String,
    ): ResultWork<Token, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.submitForm(
                    url = NEW_BASE_URL + "auth/login",
                    formParameters = parameters {
                        append(EMAIL, email)
                        append(PASSWORD, password)
                    }
                ).call
                    .body<com.edurda77.data.remote.auth.TokenDto>()
                Token(
                    accessToken = result.accessToken,
                    refreshToken = result.refreshToken
                )
            }
        }
    }

    override suspend fun refresh(
        refreshToken: String,
    ): ResultWork<Token, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.post(
                    NEW_BASE_URL + "auth/refresh"
                ) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        com.edurda77.data.remote.auth.RefreshRequest(refreshToken)
                    )
                }.call
                    .body<com.edurda77.data.remote.auth.TokenDto>()
                Token(
                    accessToken = result.accessToken,
                    refreshToken = result.refreshToken
                )
            }
        }
    }

    override suspend fun logout(
        refreshToken: String,
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.post(
                    NEW_BASE_URL + "auth/logout"
                ) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        com.edurda77.data.remote.auth.RefreshRequest(refreshToken)
                    )
                }.call
                    .body<com.edurda77.data.remote.auth.TokenDto>()
                Unit
            }
        }
    }

    override suspend fun loadAuthUserData(
        accessToken: String,
    ): ResultWork<AuthUser, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(NEW_BASE_URL + "auth/user") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                result.call.body<com.edurda77.data.remote.auth.AuthUserDto>().toAuthUser()
            }
        }
    }
}