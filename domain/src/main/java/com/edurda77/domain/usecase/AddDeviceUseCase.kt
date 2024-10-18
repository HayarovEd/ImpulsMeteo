package com.edurda77.domain.usecase

import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import javax.inject.Inject

class AddDeviceUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        groups: List<Int>,
        key: String,
        name: String,
        update: String
    ): ResultWork<Unit, DataError> {
        return remoteRepository.addDevice(
            groups = groups,
            key = key,
            name = name,
            token = token,
            update = update
        )
    }
}