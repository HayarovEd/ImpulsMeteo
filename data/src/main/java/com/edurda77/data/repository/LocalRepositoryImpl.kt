package com.edurda77.data.repository

import com.edurda77.data.handler.handleReadFromDataBase
import com.edurda77.data.handler.handleWriteToDataBase
import com.edurda77.data.local.FavoriteEntity
import com.edurda77.data.local.MeteoDataBase
import com.edurda77.domain.model.Favorite
import com.edurda77.domain.repository.LocalRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    db: MeteoDataBase
) : LocalRepository {
    private val dao = db.meteoDao

    override suspend fun insertFavorite(
        deviceId: Int,
    ): ResultWork<Unit, DataError.LocalDateBase> {
        return handleWriteToDataBase {
            dao.insertFavorite(
                FavoriteEntity(
                    deviceId = deviceId
                )
            )
        }
    }

    override suspend fun deleteFavorite(
        id: Int,
        deviceId: Int,
    ): ResultWork<Unit, DataError.LocalDateBase> {
        return handleWriteToDataBase {
            dao.delete(
                FavoriteEntity(
                    id = id,
                    deviceId = deviceId
                )
            )
        }
    }

    override suspend fun getAllFavorites(): Flow<ResultWork<List<Favorite>, DataError.LocalDateBase>> {
        return handleReadFromDataBase {
            dao.getAllFavorites().map { favorites ->
                favorites.map {
                    Favorite(
                        id = it.id,
                        deviceId = it.deviceId
                    )
                }
            }
        }
    }
}