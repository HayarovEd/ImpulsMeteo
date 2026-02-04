package com.edurda77.domain.usecase

import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeleteDevicesGroupUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int
    ): ResultWork<Unit, DataError> {
        return oldRemoteRepository.deleteDevicesGroup(
            token = token,
            id = id
        )
    }
}