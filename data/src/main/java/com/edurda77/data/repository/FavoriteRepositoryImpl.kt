package com.edurda77.data.repository

import com.edurda77.data.handler.handleResponse
import com.edurda77.data.mapper.toFavorite
import com.edurda77.data.remote.newDtos.favorite.FavoriteCreateRequest
import com.edurda77.data.remote.newDtos.favorite.FavoriteDto
import com.edurda77.domain.model.newModels.Favorite
import com.edurda77.domain.repository.FavoriteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.NEW_BASE_URL
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
    private val httpClient: HttpClient
): FavoriteRepository {

    override suspend fun insertFavorite(
        accessToken: String,
        deviceId: String
    ): ResultWork<List<Favorite>, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                val result = httpClient.post(NEW_BASE_URL + "favorite") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                    setBody(
                        FavoriteCreateRequest(
                            deviceId = deviceId
                        )
                    )
                }
                result.call.body<List<FavoriteDto>>().map { it.toFavorite() }
            }
        }
    }

    override suspend fun deleteFavorite(
        accessToken: String,
        favoriteId: String
    ): ResultWork<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            handleResponse {
                httpClient.delete (NEW_BASE_URL + "favorite/${favoriteId}") {
                    contentType(ContentType.Application.Json)
                    bearerAuth(accessToken)
                }
               Unit
            }
        }
    }
}