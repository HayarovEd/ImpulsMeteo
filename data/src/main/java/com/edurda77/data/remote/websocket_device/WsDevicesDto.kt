package com.edurda77.data.remote.websocket_device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WsDevicesDto(
    @SerialName("groups")
    val wsGroups: List<WsGroup>,
    @SerialName("host")
    val host: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("key")
    val key: String,
    @SerialName("lastUpdate")
    val lastUpdate: String?,
    @SerialName("name")
    val name: String,
    @SerialName("params")
    val wsParams: List<WsParam>,
    @SerialName("port")
    val port: Int,
    @SerialName("status")
    val status: String,
    @SerialName("update")
    val update: Int,
    @SerialName("video")
    val video: String?
)