package com.edurda77.data.remote.add_devices_group


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddDevicesGroupDto(
    @SerialName("name")
    val name: String
)