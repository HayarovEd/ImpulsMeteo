package com.edurda77.data.remote.newDtos.group


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDeviceRequest(
    @SerialName("name")
    val name: String
)