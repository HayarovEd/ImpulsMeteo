package com.edurda77.data.remote.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceUserRequest(
    @SerialName("id")
    val id: String
)