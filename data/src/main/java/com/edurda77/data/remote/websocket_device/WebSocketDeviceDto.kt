package com.edurda77.data.remote.websocket_device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebSocketDeviceDto(
    @SerialName("channel")
    val channel: String,
    @SerialName("data")
    val wsDevices: List<WsDevicesDto>,
    @SerialName("event")
    val event: String
)