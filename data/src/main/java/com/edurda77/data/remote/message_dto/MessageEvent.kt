package com.edurda77.data.remote.message_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageEvent(
    @SerialName("data")
    val content: Content,
    @SerialName("event")
    val event: String
)