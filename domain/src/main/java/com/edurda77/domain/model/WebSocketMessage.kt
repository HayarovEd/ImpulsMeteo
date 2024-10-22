package com.edurda77.domain.model

sealed class WebSocketMessage {
    class Connect(val messageWebSocketStart: MessageWebSocketStart) : WebSocketMessage()
    class SuccessSbscribe(val successSubscribe: SuccessSubscribe) : WebSocketMessage()
    class DeviceEvent(val device: Device) : WebSocketMessage()
}