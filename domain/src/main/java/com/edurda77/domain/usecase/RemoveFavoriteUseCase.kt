package com.edurda77.domain.usecase

import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class RemoveFavoriteUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        deviceId: Int,
    ): ResultWork<Unit, DataError> {
        return remoteRepository.deleteFavorite(
            deviceId = deviceId,
            token = token
        )
    }
}