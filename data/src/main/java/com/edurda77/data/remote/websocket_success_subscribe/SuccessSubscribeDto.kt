package com.edurda77.data.remote.websocket_success_subscribe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuccessSubscribeDto(
    @SerialName("channel")
    val channel: String,
    @SerialName("event")
    val event: String
)