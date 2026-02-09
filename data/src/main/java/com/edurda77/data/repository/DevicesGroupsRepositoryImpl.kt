package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toGroupDevice
import com.edurda77.data.remote.newDtos.group.GroupDeviceDto
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.repository.DevicesGroupsRepository
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

class DevicesGroupsRepositoryImpl(
    private val httpClient: HttpClient
): DevicesGroupsRepository {

    override suspend fun loadGroups(
        accessToken: String,
    ): ResultWork<List<GroupDevice>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(NEW_BASE_URL + "device-groups") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                result.call.body<List<GroupDeviceDto>>().map { it.toGroupDevice() }
            }
        }
    }
}