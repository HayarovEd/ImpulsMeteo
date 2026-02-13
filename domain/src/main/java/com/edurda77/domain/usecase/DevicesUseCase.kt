package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.repository.DevicesRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

class DevicesUseCase(
    private val devicesRepository: DevicesRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): ResultWork<List<Device>, DataError> {
        return tokenManager.validateFactory(
            data = {
                devicesRepository.getDevices(it)
            },
        )
    }
}