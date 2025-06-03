package com.edurda77.domain.usecase

import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeleteUnitUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int
    ): ResultWork<Unit, DataError> {
        return remoteRepository.deleteUnit(
            token = token,
            id = id
        )
    }
}