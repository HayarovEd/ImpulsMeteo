package com.edurda77.data.remote.device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupsDeviceId(
    @SerialName("id")
    val id: String
)