package com.edurda77.domain.repository

import com.edurda77.domain.model.FavoriteOld
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun insertFavorite(deviceId: Int): ResultWork<Unit, DataError.LocalDateBase>
    suspend fun deleteFavorite(deviceId: Int): ResultWork<Unit, DataError.LocalDateBase>
    suspend fun getAllFavorites(): Flow<ResultWork<List<FavoriteOld>, DataError.LocalDateBase>>
}