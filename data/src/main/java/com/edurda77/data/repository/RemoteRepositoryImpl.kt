package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.remote.newDtos.auth.TokenDto
import com.edurda77.domain.model.Token
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EMAIL
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.PASSWORD
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
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
                    .body<TokenDto>()
                Token(
                    accessToken = result.accessToken,
                    refreshToken = result.refreshToken
                )
            }
        }
    }
}