package com.edurda77.domain.usecase


import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.repository.DevicesRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateDeviceUseCase(
    private val devicesRepository: DevicesRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        deviceId: String,
        key: String,
        name: String,
        updateRate: Int,
        groups: List<GroupDevice>
    ): ResultWork<Device, DataError> {
        return tokenManager.validateFactory(
            data = {
                devicesRepository.updateDeviceById(
                    accessToken = it,
                    deviceId = deviceId,
                    key = key,
                    name = name,
                    updateRate = updateRate,
                    groups = groups
                )
            },
        )
    }
}