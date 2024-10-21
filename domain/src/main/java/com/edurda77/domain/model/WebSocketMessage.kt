package com.edurda77.domain.model

sealed class WebSocketMessage {
    class Connect(val messageWebSocketStart: MessageWebSocketStart) : WebSocketMessage()
    data object Error : WebSocketMessage()
}