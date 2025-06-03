package com.edurda77.domain.usecase

import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateDevicesGroupUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        name: String,
    ): ResultWork<Unit, DataError> {
        if (name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)

        return remoteRepository.updateDevicesGroup(
            id = id,
            name = name,
            token = token,
        )
    }
}