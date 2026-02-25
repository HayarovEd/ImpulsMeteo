package com.edurda77.data.remote.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    @SerialName("host")
    val host: String?,
    @SerialName("id")
    val id: String,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("notification_device")
    val notificationDeviceDto: com.edurda77.data.remote.notification.NotificationDeviceDto?,
    @SerialName("port")
    val port: Int,
    @SerialName("status")
    val status: Boolean,
    @SerialName("update_rate")
    val updateRate: Int,
    @SerialName("update_at")
    val updatedDate: String?,
    @SerialName("video_url")
    val videoUrl: String,
    @SerialName("groups_device")
    val groupsDevice: List<com.edurda77.data.remote.group.GroupDeviceDto>,
    @SerialName("params_device")
    val params: List<com.edurda77.data.remote.param.ParamDto>?,
)