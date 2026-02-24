package com.edurda77.domain.usecase

import com.edurda77.domain.repository.DevicesRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeleteDeviceUseCase(
    private val devicesRepository: DevicesRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        deviceId: String
    ): ResultWork<Unit, DataError> {
        return tokenManager.validateFactory(
            data = {
                devicesRepository.deleteDeviceById(
                    accessToken = it,
                    deviceId = deviceId,
                )
            },
        )
    }
}