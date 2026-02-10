package com.edurda77.domain.repository

import com.edurda77.domain.model.WebSocketMessageOld
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow

interface WebSocketRepositoryOld {
    suspend fun close()
    suspend fun sendAction(event: String, deviceId: Int, auth: String)
    fun getStateStream(): Flow<ResultWork<WebSocketMessageOld, DataError.WebSocketError>>
}