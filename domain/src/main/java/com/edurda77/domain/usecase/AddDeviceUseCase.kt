package com.edurda77.domain.usecase

import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class AddDeviceUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        groups: List<Int>,
        key: String,
        name: String,
        update: String
    ): ResultWork<Unit, DataError> {
        return oldRemoteRepository.addDevice(
            groups = groups,
            key = key,
            name = name,
            token = token,
            update = update
        )
    }
}