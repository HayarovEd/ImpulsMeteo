package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.convertToAuth
import com.edurda77.data.remote.auth.AuthDto
import com.edurda77.domain.model.Auth
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.AUTH_POSTFIX
import com.edurda77.domain.utils.BASE_URL
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EMAIL
import com.edurda77.domain.utils.PASSWORD
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
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
                val result = httpClient.post(BASE_URL + AUTH_POSTFIX) {
                    contentType(ContentType.Application.Json)
                    setBody {
                        FormDataContent(Parameters.build {
                            append(EMAIL, email)
                            append(PASSWORD, password)
                        })
                    }
                }
                    .call
                    .body<AuthDto>()
                result.convertToAuth()
            }
        }
    }

}