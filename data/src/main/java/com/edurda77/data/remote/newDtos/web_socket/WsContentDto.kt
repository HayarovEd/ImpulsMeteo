package com.edurda77.data.remote.newDtos.web_socket


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class  WsContentDto <T>(
    @SerialName("data")
    val content: T,
    @SerialName("event")
    val event: String,
    @SerialName("path")
    val path: String
)