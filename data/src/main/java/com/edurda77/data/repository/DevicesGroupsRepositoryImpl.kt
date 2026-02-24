package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toGroupDevice
import com.edurda77.data.mapper.toGroupDeviceDto
import com.edurda77.domain.model.GroupDevice
import com.edurda77.domain.repository.DevicesGroupsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DevicesGroupsRepositoryImpl(
    private val httpClient: HttpClient
) : DevicesGroupsRepository {

    override suspend fun loadGroups(
        accessToken: String,
    ): ResultWork<List<GroupDevice>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(NEW_BASE_URL + "device-groups") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                result.call.body<List<com.edurda77.data.remote.group.GroupDeviceDto>>().map { it.toGroupDevice() }
            }
        }
    }

    override suspend fun insertGroup(
        accessToken: String,
        name: String,
    ): ResultWork<GroupDevice, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.post(NEW_BASE_URL + "device-groups") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        com.edurda77.data.remote.group.GroupDeviceRequest(
                            name = name
                        )
                    )
                }
                result.call.body<com.edurda77.data.remote.group.GroupDeviceDto>().toGroupDevice()
            }
        }
    }

    override suspend fun updateGroup(
        accessToken: String,
        groupDevice: GroupDevice,
    ): ResultWork<GroupDevice, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.patch(NEW_BASE_URL + "device-groups/"+groupDevice.id) {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        groupDevice.toGroupDeviceDto()
                    )
                }
                result.call.body<com.edurda77.data.remote.group.GroupDeviceDto>().toGroupDevice()
            }
        }
    }

    override suspend fun deleteGroup(
        accessToken: String,
        groupId: String
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.delete (NEW_BASE_URL + "device-groups/"+groupId) {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                Unit
            }
        }
    }
}