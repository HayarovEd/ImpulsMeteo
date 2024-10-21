package com.edurda77.data.remote.websocket.init_message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitConnectionData(
    @SerialName("activity_timeout")
    val activityTimeout: Int,
    @SerialName("socket_id")
    val socketId: String
)