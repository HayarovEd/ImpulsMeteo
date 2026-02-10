package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.WebSocketMessage
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {

    suspend fun close()
    fun getStateStream(accessToken: String): Flow<ResultWork<WebSocketMessage, DataError.WebSocketError>>
}