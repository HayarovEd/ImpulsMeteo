package com.edurda77.data.remote.newDtos.device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceUpdateRequest(
    @SerialName("groups_device")
    val groupsDeviceId: List<GroupsDeviceId>,
    @SerialName("id")
    val id: String,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("update_rate")
    val updateRate: Int
)