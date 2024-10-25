package com.edurda77.domain.repository

import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.Notifications
import com.edurda77.domain.model.Permissions
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.model.UnitMeteo
import com.edurda77.domain.model.User
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface RemoteRepository {
    suspend fun authorization(email: String, password: String): ResultWork<Auth, DataError>
    suspend fun authorizedUser(token: String): ResultWork<LoggedUser, DataError>
    suspend fun getGroupedDevices(
        token: String,
        parameterGroup: Int = 0
    ): ResultWork<List<Device>, DataError>

    suspend fun addDevice(
        token: String,
        groups: List<Int>,
        key: String,
        name: String,
        update: String
    ): ResultWork<Unit, DataError>

    suspend fun getPermissions(token: String): ResultWork<Permissions, DataError>
    suspend fun getUsers(token: String): ResultWork<List<User>, DataError>
    suspend fun addUser(
        token: String,
        devices: List<String>,
        permissions: List<String>,
        email: String,
        name: String,
        password: String
    ): ResultWork<Unit, DataError>

    suspend fun deleteUser(token: String, id: Int): ResultWork<Unit, DataError>
    suspend fun updateUser(
        token: String,
        id: Int,
        devices: List<String>,
        permissions: List<String>,
        name: String,
        email: String,
        password: String
    ): ResultWork<Unit, DataError>

    suspend fun getDevicesGroups(
        token: String,
    ): ResultWork<List<GroupDevices>, DataError>

    suspend fun getUnits(token: String): ResultWork<List<UnitMeteo>, DataError>
    suspend fun addDevicesGroup(token: String, name: String): ResultWork<Unit, DataError>
    suspend fun addUnit(token: String, name: String, short: String): ResultWork<Unit, DataError>
    suspend fun deleteUnit(token: String, id: Int): ResultWork<Unit, DataError>
    suspend fun deleteDevicesGroup(token: String, id: Int): ResultWork<Unit, DataError>
    suspend fun updateDevicesGroup(
        token: String,
        id: Int,
        name: String
    ): ResultWork<Unit, DataError>

    suspend fun updateUnit(
        token: String,
        id: Int,
        name: String,
        short: String
    ): ResultWork<Unit, DataError>

    suspend fun getDeviceById(token: String, id: Int): ResultWork<SingleDevice, DataError>
    suspend fun getBroadcatingAuth(
        socketId: String,
        deviceId: Int,
        token: String
    ): ResultWork<String, DataError>

    suspend fun updateNotificationsDevice(
        token: String,
        id: Int,
        notifications: Notifications
    ): ResultWork<Unit, DataError>

    suspend fun updateDeviceById(token: String, device: SingleDevice): ResultWork<Unit, DataError>
}