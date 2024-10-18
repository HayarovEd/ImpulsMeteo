package com.edurda77.data.remote.update_devices_group


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateDevicesGroupDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)