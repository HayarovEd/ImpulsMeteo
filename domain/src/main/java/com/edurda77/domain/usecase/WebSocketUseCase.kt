package com.edurda77.domain.usecase


import com.edurda77.domain.model.WebSocketMessage
import com.edurda77.domain.repository.WebSocketRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow

class WebSocketUseCase(
    private val webSocketRepository: WebSocketRepository,
    private val tokenManager: TokenManager,
) {
    operator fun invoke(): Flow<ResultWork<WebSocketMessage, DataError>> {
        return tokenManager.validateFactoryFlow(
            data = {
                webSocketRepository.getStateStream(it)
            },
        )
    }
}