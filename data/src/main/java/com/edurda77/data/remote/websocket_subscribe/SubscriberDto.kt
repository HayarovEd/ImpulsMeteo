package com.edurda77.data.remote.websocket_subscribe


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriberDto(
    @SerialName("data")
    val subscriberData: SubscriberData,
    @SerialName("event")
    val event: String
)