package com.edurda77.data.mapper

import com.edurda77.data.remote.websocket.init_message.InitConnectionData
import com.edurda77.data.remote.websocket.init_message.InitMessageDto
import com.edurda77.data.remote.websocket.init_message.OriginalStartMessage
import com.edurda77.data.remote.websocket_device.WebSocketDeviceDto
import com.edurda77.data.remote.websocket_success_subscribe.SuccessSubscribeDto
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.MessageWebSocketStart
import com.edurda77.domain.model.Param
import com.edurda77.domain.model.SuccessSubscribe
import com.edurda77.domain.utils.STATUS_ON
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

fun convertToSuccessSubscribe(message: String): SuccessSubscribe {
    val originalData = Json.decodeFromString<SuccessSubscribeDto>(message)
    return SuccessSubscribe(
        channel = originalData.channel,
        event = originalData.event
    )
}

fun convertDeviceMessageToDevice(message: String): Device {
    val formattedData = message
        .replace("\"[\\\"", "[")
        .replace("\\\"]\"", "]")
        .replace("\\\\\\", "")
    println("web socket open, formattedData $formattedData")
    val originalData = Json.decodeFromString<WebSocketDeviceDto>(formattedData)
    return Device(
        id = originalData.wsDevices.first().id,
        name = originalData.wsDevices.first().name,
        key = originalData.wsDevices.first().key,
        status = originalData.wsDevices.first().status == STATUS_ON,
        video = originalData.wsDevices.first().video,
        groups = originalData.wsDevices.first().wsGroups.map { group ->
            GroupDevices(
                id = group.id,
                name = group.name
            )
        },
        params = originalData.wsDevices.first().wsParams.map { param ->
            Param(
                classIcon = param.classIcon,
                name = param.name,
                label = param.label,
                value = param.value.toDoubleOrNull() ?: 0.0,
                idUnit = param.idUnit,
                id = param.id
            )
        },
        updatedAt = originalData.wsDevices.first().lastUpdate ?: ""
    )
}
