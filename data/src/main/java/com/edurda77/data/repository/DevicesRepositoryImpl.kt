package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toDevice
import com.edurda77.data.mapper.toGroupDeviceDto
import com.edurda77.data.mapper.toNotificationDevice
import com.edurda77.data.mapper.toNotificationsDeviceDtos
import com.edurda77.data.remote.device.DeviceDto
import com.edurda77.data.remote.device.GroupsDeviceId
import com.edurda77.data.remote.notification.NotificationDeviceDto
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevice
import com.edurda77.domain.model.NotificationDevice
import com.edurda77.domain.model.NotificationParam
import com.edurda77.domain.repository.DevicesRepository
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

class DevicesRepositoryImpl(
    private val httpClient: HttpClient
) : DevicesRepository {

    override suspend fun insertDevice(
        accessToken: String,
        name: String,
        key: String,
        frequency: Int,
        groups: List<GroupDevice>,
    ): ResultWork<Device, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.post(NEW_BASE_URL + "devices") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        com.edurda77.data.remote.device.DeviceCreateRequest(
                            groupsDevice = groups.map { it.toGroupDeviceDto() },
                            key = key,
                            name = name,
                            updateRate = frequency,
                            status = false,
                            videoUrl = ""
                        )
                    )
                }
                result.call.body<DeviceDto>().toDevice()
            }
        }
    }

    override suspend fun getDevices(
        accessToken: String,
    ): ResultWork<List<Device>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(NEW_BASE_URL + "devices") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                result.call.body<List<DeviceDto>>().map { it.toDevice() }
            }
        }
    }

    override suspend fun getDeviceById(
        accessToken: String,
        deviceId: String,
    ): ResultWork<Device, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.get(NEW_BASE_URL + "devices/" + deviceId) {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                result.call.body<DeviceDto>().toDevice()
            }
        }
    }

    override suspend fun updateDeviceById(
        accessToken: String,
        deviceId: String,
        key: String,
        name: String,
        updateRate: Int,
        groups: List<GroupDevice>
    ): ResultWork<Device, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.patch (NEW_BASE_URL + "devices/" + deviceId) {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        com.edurda77.data.remote.device.DeviceUpdateRequest(
                            groupsDeviceId = groups.map {
                                GroupsDeviceId(
                                    it.id
                                )
                            },
                            id = deviceId,
                            key = key,
                            name = name,
                            updateRate = updateRate
                        )
                    )
                }
                result.call.body<DeviceDto>().toDevice()
            }
        }
    }

    override suspend fun deleteDeviceById(
        accessToken: String,
        deviceId: String,
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.delete (NEW_BASE_URL + "devices/" + deviceId) {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
                Unit
            }
        }
    }

    override suspend fun updateNotificationOfDevice(
        accessToken: String,
        notificationsParam: List<NotificationParam>,
        value: Boolean,
        deviceId: String,
        userId: String,
    ): ResultWork<NotificationDevice, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result =
                    httpClient.post(NEW_BASE_URL + "devices/" + deviceId + "/notifications") {
                        contentType(ContentType.Application.Json)
                        bearerAuth(accessToken)
                        setBody(
                            NotificationDeviceDto(
                                deviceId = deviceId,
                                notificationsDeviceDtos = notificationsParam.map { it.toNotificationsDeviceDtos() },
                                value = value,
                                userId = userId,
                                id = ""
                            )
                        )
                    }
                result.call.body<NotificationDeviceDto>().toNotificationDevice()
            }
        }
    }
}