package com.edurda77.domain.usecase

import com.edurda77.domain.repository.WebSocketRepository


class CloseWebsocketUseCase(
    private val webSocketRepository: WebSocketRepository,
) {
    suspend operator fun invoke() {
        webSocketRepository.close()
    }
}