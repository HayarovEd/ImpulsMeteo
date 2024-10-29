package com.edurda77.data.remote.websocket_device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WsPivotGroup(
    @SerialName("id_device")
    val idDevice: Int,
    @SerialName("id_group")
    val idGroup: Int
)