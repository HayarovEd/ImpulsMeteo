package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.GroupDevice
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
}