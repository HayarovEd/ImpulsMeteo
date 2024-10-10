package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.convertToAuth
import com.edurda77.data.mapper.convertToLoggedUser
import com.edurda77.data.remote.auth.AuthDto
import com.edurda77.data.remote.auth_user.AuthUserDto
import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.AUTH_LOGGEED_USER_POSTFIX
import com.edurda77.domain.utils.AUTH_POSTFIX
import com.edurda77.domain.utils.BASE_URL
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EMAIL
import com.edurda77.domain.utils.PASSWORD
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : RemoteRepository {

    override suspend fun autorization(
        email: String,
        password: String,
    ): ResultWork<Auth, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.submitForm(
                    url = BASE_URL + AUTH_POSTFIX,
                    formParameters = parameters {
                        append(EMAIL, email)
                        append(PASSWORD, password)
                    }
                ).call
                    .body<AuthDto>()
                result.convertToAuth()
            }
        }
    }

    override suspend fun autorizedUser(
        token: String,
    ): ResultWork<LoggedUser, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(BASE_URL+AUTH_LOGGEED_USER_POSTFIX) {
                    url {
                       bearerAuth(token)
                    }
                }.call
                    .body<AuthUserDto>()
                result.convertToLoggedUser()
            }
        }
    }

}