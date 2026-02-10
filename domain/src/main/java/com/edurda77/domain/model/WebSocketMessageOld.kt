package com.edurda77.domain.model

sealed class WebSocketMessageOld {
    class Connect(val messageWebSocketStart: MessageWebSocketStart) : WebSocketMessageOld()
    class SuccessSbscribe(val successSubscribe: SuccessSubscribe) : WebSocketMessageOld()
    class DeviceEvent(val deviceOld: DeviceOld) : WebSocketMessageOld()
}