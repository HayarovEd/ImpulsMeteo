package com.edurda77.data.remote.newDtos.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CameraDto(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("stream")
    val stream: String,
    @SerialName("substream")
    val substream: String,
    @SerialName("is_public")
    val isPublic: Boolean,
)