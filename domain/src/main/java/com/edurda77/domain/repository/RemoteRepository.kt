package com.edurda77.domain.repository

import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface RemoteRepository {
    suspend fun authorization(email: String, password: String): ResultWork<Auth, DataError>
    suspend fun authorizedUser(token: String): ResultWork<LoggedUser, DataError>
    suspend fun getGroupedDevices(
        token: String,
        parameterGroup: Int = 0
    ): ResultWork<Map<GroupDevices, List<Device>>, DataError>
}