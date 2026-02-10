package com.edurda77.domain.usecase

import com.edurda77.domain.repository.WebSocketRepositoryOld


class CloseWebsocketUseCase(
    private val webSocketRepositoryOld: WebSocketRepositoryOld,
) {
    suspend operator fun invoke() {
        webSocketRepositoryOld.close()
    }
}