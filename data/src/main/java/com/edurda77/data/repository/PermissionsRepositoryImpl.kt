package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toPermission
import com.edurda77.domain.model.Permission
import com.edurda77.domain.repository.PermissionsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PermissionsRepositoryImpl(
    private val httpClient: HttpClient
) : PermissionsRepository {

    override suspend fun getPermissions(
        accessToken: String,
    ): ResultWork<List<Permission>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(NEW_BASE_URL + "permissions") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                result.call.body<List<com.edurda77.data.remote.permission.PermissionDto>>().map { it.toPermission() }
            }
        }
    }
}