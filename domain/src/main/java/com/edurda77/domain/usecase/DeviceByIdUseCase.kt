package com.edurda77.domain.usecase

import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import javax.inject.Inject

class DeviceByIdUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
    ): ResultWork<SingleDevice, DataError> {
        return remoteRepository.getDeviceById(
            token = token,
            id = id
        )
    }
}