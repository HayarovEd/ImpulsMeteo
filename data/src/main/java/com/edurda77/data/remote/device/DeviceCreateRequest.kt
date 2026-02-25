package com.edurda77.data.remote.device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceCreateRequest(
    @SerialName("groups_device")
    val groupsDevice: List<com.edurda77.data.remote.group.GroupDeviceDto>,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("status")
    val status: Boolean,
    @SerialName("update_rate")
    val updateRate: Int,
    @SerialName("video_url")
    val videoUrl: String
)