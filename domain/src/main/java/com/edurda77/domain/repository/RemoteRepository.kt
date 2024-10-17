package com.edurda77.domain.repository

import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.Permissions
import com.edurda77.domain.model.User
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface RemoteRepository {
    suspend fun authorization(email: String, password: String): ResultWork<Auth, DataError>
    suspend fun authorizedUser(token: String): ResultWork<LoggedUser, DataError>
    suspend fun getGroupedDevices(
        token: String,
        parameterGroup: Int = 0
    ): ResultWork<Map<GroupDevices, List<Device>>, DataError>

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
}