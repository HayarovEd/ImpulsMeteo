package com.edurda77.domain.usecase

import com.edurda77.domain.model.Device
import com.edurda77.domain.repository.DevicesRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeviceByIdUseCase(
    private val devicesRepository: DevicesRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        id: String,
    ): ResultWork<Device, DataError> {
        return tokenManager.validateFactory(
            data = {
                devicesRepository.getDeviceById(
                    accessToken = it,
                    deviceId = id
                )
            }
        )
    }
}