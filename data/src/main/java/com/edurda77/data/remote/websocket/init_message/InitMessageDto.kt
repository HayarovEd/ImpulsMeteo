package com.edurda77.data.remote.websocket.init_message


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OriginalStartMessage(
    @SerialName("data")
    val startMessage: String,
    @SerialName("event")
    val event: String
)

@Serializable
data class InitMessageDto(
    @SerialName("data")
    val initConnectionData: InitConnectionData,
    @SerialName("event")
    val event: String
)