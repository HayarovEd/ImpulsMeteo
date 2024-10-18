package com.edurda77.data.remote.message_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    @SerialName("auth")
    val auth: String,
    @SerialName("channel")
    val channel: String
)