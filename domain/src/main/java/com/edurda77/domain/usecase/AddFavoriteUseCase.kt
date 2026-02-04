package com.edurda77.domain.usecase

import com.edurda77.domain.model.Favorite
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class AddFavoriteUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token:String,
        deviceId: Int,
    ): ResultWork<Favorite, DataError> {
        return oldRemoteRepository.addFavorite(
            token = token,
            deviceId = deviceId
        )
    }
}