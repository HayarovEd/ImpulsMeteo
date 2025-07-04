package com.edurda77.domain.usecase

import com.edurda77.domain.model.Favorite
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class AddFavoriteUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token:String,
        deviceId: Int,
    ): ResultWork<Favorite, DataError> {
        return remoteRepository.addFavorite(
            token = token,
            deviceId = deviceId
        )
    }
}