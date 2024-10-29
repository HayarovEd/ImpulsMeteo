package com.edurda77.data.remote.websocket_subscribe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriberData(
    @SerialName("auth")
    val auth: String,
    @SerialName("channel")
    val channel: String
)