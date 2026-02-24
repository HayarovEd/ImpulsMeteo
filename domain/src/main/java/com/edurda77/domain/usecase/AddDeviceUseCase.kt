package com.edurda77.domain.usecase

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevice
import com.edurda77.domain.repository.DevicesRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class AddDeviceUseCase(
    private val devicesRepository: DevicesRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        name: String,
        key: String,
        frequency: Int,
        groups: List<GroupDevice>,
    ): ResultWork<Device, DataError> {
        return tokenManager.validateFactory(
            data = {
                devicesRepository.insertDevice(
                    groups = groups,
                    key = key,
                    name = name,
                    accessToken = it,
                    frequency = frequency
                )
            }
        )
    }
}