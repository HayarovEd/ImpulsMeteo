package com.edurda77.domain.repository

import com.edurda77.domain.model.Favorite
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface FavoriteRepository {
    suspend fun insertFavorite(
        accessToken: String,
        deviceId: String
    ): ResultWork<List<Favorite>, DataError>

    suspend fun deleteFavorite(
        accessToken: String,
        favoriteId: String
    ): ResultWork<Unit, DataError>
}