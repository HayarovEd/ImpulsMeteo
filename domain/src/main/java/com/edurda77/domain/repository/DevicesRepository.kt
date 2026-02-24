package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.model.newModels.NotificationDevice
import com.edurda77.domain.model.newModels.NotificationParam
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface DevicesRepository {
    suspend fun insertDevice(
        accessToken: String,
        name: String,
        key: String,
        frequency: Int,
        groups: List<GroupDevice>
    ): ResultWork<Device, DataError>

    suspend fun getDevices(accessToken: String): ResultWork<List<Device>, DataError>
    suspend fun getDeviceById(accessToken: String, deviceId: String): ResultWork<Device, DataError>
    suspend fun updateNotificationOfDevice(
        accessToken: String,
        notificationsParam: List<NotificationParam>,
        value: Boolean,
        deviceId: String,
        userId: String,
    ): ResultWork<NotificationDevice, DataError>

    suspend fun updateDeviceById(
        accessToken: String,
        deviceId: String,
        key: String,
        name: String,
        updateRate: Int,
        groups: List<GroupDevice>
    ): ResultWork<Device, DataError>

    suspend fun deleteDeviceById(accessToken: String, deviceId: String): ResultWork<Unit, DataError>
}