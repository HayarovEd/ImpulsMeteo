package com.edurda77.domain.usecase


import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateDeviceUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        device: SingleDevice
    ): ResultWork<Unit, DataError> {
        return oldRemoteRepository.updateDeviceById(
            token = token,
            device = device
        )
    }
}