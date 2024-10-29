package com.edurda77.domain.usecase


import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import javax.inject.Inject

class UpdateDeviceUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        device: SingleDevice
    ): ResultWork<Unit, DataError> {
        return remoteRepository.updateDeviceById(
            token = token,
            device = device
        )
    }
}