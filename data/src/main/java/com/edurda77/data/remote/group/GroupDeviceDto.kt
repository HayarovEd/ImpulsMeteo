package com.edurda77.data.remote.group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDeviceDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)