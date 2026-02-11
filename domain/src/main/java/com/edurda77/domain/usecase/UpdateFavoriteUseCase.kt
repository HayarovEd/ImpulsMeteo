package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.Favorite
import com.edurda77.domain.repository.FavoriteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        deviceId: String,
        favorites: List<Favorite>
    ): ResultWork<List<Favorite>, DataError> {
        return tokenManager.validateFactory(
            data = {
                if (favorites.map { fv -> fv.deviceId }.contains(deviceId)) {
                    val favorite = favorites.first { fv -> fv.deviceId == deviceId }
                    when (val result =
                        favoriteRepository.deleteFavorite(
                            accessToken = it,
                            favoriteId = favorite.id
                        )) {
                        is ResultWork.Error -> ResultWork.Error(result.error)
                        is ResultWork.Success -> {
                            ResultWork.Success(favorites - favorite)
                        }
                    }
                } else {
                    favoriteRepository.insertFavorite(
                        accessToken = it,
                        deviceId = deviceId
                    )
                }
            },
        )
    }
}