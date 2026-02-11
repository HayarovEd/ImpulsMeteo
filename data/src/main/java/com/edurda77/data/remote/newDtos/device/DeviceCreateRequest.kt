package com.edurda77.data.remote.newDtos.device


import com.edurda77.data.remote.newDtos.group.GroupDeviceDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceCreateRequest(
    @SerialName("groups_device")
    val groupsDevice: List<GroupDeviceDto>,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("status")
    val status: String,
    @SerialName("update_rate")
    val updateRate: Int,
    @SerialName("video_url")
    val videoUrl: String
)