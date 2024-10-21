package com.edurda77.data.mapper

import com.edurda77.data.remote.websocket.init_message.InitConnectionData
import com.edurda77.data.remote.websocket.init_message.InitMessageDto
import com.edurda77.data.remote.websocket.init_message.OriginalStartMessage
import com.edurda77.domain.model.MessageWebSocketStart
import kotlinx.serialization.json.Json


fun convertInitMessage(message: String): MessageWebSocketStart {
    val dto = formatInitMessage(message)
    return MessageWebSocketStart(
        activityTimeout = dto.initConnectionData.activityTimeout,
        socketId = dto.initConnectionData.socketId,
        event = dto.event
    )
}


private fun formatInitMessage(message: String): InitMessageDto {
    val originalData = Json.decodeFromString<OriginalStartMessage>(message)
    val innerData = Json.decodeFromString<InitConnectionData>(originalData.startMessage)
    return InitMessageDto(
        event = originalData.event,
        initConnectionData = innerData
    )
}
