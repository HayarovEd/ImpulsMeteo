package com.edurda77.domain.usecase

import com.edurda77.domain.repository.WebSocketRepository
import javax.inject.Inject

class CloseWebsocketUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    suspend operator fun invoke() {
        webSocketRepository.close()
    }
}